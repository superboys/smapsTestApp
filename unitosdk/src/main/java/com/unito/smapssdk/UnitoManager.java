package com.unito.smapssdk;

import static com.inuker.bluetooth.library.Code.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;
import static com.unito.smapssdk.library.BLEConstant.WATERSYSTEM_CHARACTERSTIC;
import static com.unito.smapssdk.library.BLEConstant.WATERSYSTEM_SERVICE;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.unito.smapssdk.library.ComConvertJson;
import com.unito.smapssdk.library.JsonConvertCom;
import com.unito.smapssdk.library.JsonUtils;
import com.unito.smapssdk.library.LiveDataBus;
import com.unito.smapssdk.library.NotifyResponse;
import com.unito.smapssdk.library.ThreadPoolUtil;
import com.unito.smapssdk.library.Utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UnitoManager {

    private static BluetoothClient bleClient;
    private List<SearchResult> saveSearchResult = new ArrayList<>();
    public String mac;
    public static int msgId = 1;
    public static String target = "";
    public static byte[] sendBytes;
    private static NotifyResponse notifyResponse;
    private int maxRssi;
    private String blueToothName;
    private Map jsonMap;
    public static boolean isWrite;
    public int timeOut = 3000;
    private boolean disConnect = false;

    private volatile static UnitoManager singleton;
    private UnitoManager (){}

    public boolean getConnectStatus() {
        if (null != bleClient) {
            if (bleClient.getConnectStatus(mac) == STATUS_DEVICE_CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public boolean getBlueToothOpened() {
        if (null != bleClient) {
            if (bleClient.isBluetoothOpened()) {
                return true;
            }
        }
        return false;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void registerUnitoWaterSystemNotify(NotifyResponse notifyResponse) {
        this.notifyResponse = notifyResponse;
        Map map = new LinkedHashMap();
        if (getBlueToothOpened()) {
            map.put("msgId", 0);
            map.put("destination", "appBle");
            map.put("source", "SDK");
            map.put("message", "blePowerOn");
            map.put("msgType", "event");
            map.put("sdkVersion", "202301310");
        } else {
            map.put("msgId", 0);
            map.put("destination", "appBle");
            map.put("source", "SDK");
            map.put("message", "blePowerOff");
            map.put("msgType", "event");
            map.put("sdkVersion", "202301310");
        }
        ThreadPoolUtil.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notity(JsonUtils.mapToJson(map));
            }
        },500);
    }

    public static UnitoManager getSingleton(Context context) {
        if (singleton == null) {
            synchronized (UnitoManager.class) {
                if (singleton == null) {
                    singleton = new UnitoManager();
                    bleClient = new BluetoothClient(context);
                    LiveDataBus.get()
                            .with("showComConvertJson", String.class)
                            .observe((LifecycleOwner) context, new Observer<String>() {
                                @Override
                                public void onChanged(@Nullable String s) {
                                    ThreadPoolUtil.handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            notity(s);
                                        }
                                    });
                                }
                            });

                    LiveDataBus.get()
                            .with("sendDataToDir", byte[].class)
                            .observe((LifecycleOwner) context, new Observer<byte[]>() {
                                @RequiresApi(api = Build.VERSION_CODES.N)
                                @Override
                                public void onChanged(@Nullable byte[] bytes) {
                                    Log.e("sendDataToDir-->", Utils.bytesToHex(bytes));
                                    sendBytes = bytes;
                                }
                            });


                    ThreadPoolUtil.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String jsonRequests = Utils.loadJSONFromAsset(context, "appSettings.json");
                            Map<String, Object> jsonParam = JsonUtils.jsonToMap(jsonRequests);
                            appSettings(jsonParam);
                        }
                    });
                }
            }
        }
        return singleton;
    }

    public void bleConnectToWaterSystem() {
        if (!isWrite) {
            isWrite = true;
        } else {
            msgId++;
        }
        if (getConnectStatus()) {
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "bleConnectToWaterSystem");
            map.put("errorMessage", "systemAlreadyConnected");
            map.put("response", "nack");
            notity(JsonUtils.mapToJson(map));
        } else {
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "bleConnectToWaterSystem");
            map.put("response", "ack");
            notity(JsonUtils.mapToJson(map));
            search(false);
        }
    }

    public void search(boolean isAutoConnect) {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(1500, 1)   // 先扫BLE设备3次，每次3s
//                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
//                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        bleClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {
                BluetoothLog.d("正在扫描...");
                saveSearchResult.clear();
            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                if (device.getName().contains("UNITO")) {
                    if (!saveSearchResult.contains(device)) {
                        saveSearchResult.add(device);
                    }
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSearchStopped() {
                BluetoothLog.d("停止扫描...");
                if (null != saveSearchResult && saveSearchResult.size() > 0) {
                    for (int i = 0; i < saveSearchResult.size(); i++) {
                        Log.e("saveSearchResult-->", saveSearchResult.get(i).getName() + "  " + saveSearchResult.get(i).getAddress() + "   " + saveSearchResult.get(i).rssi);
                    }
                    maxRssi = -1000;
                    int position = -1;
                    for (int i = 0; i < saveSearchResult.size(); i++) {
                        if (saveSearchResult.get(i).rssi > maxRssi) {
                            maxRssi = saveSearchResult.get(i).rssi;
                            position = i;
                        }
                    }
                    SearchResult searchResult = saveSearchResult.get(position);
                    Log.e("maxRSSI-->", searchResult.getName() + "   " + searchResult.getAddress() + "   " + searchResult.rssi);
//                    connectBle(searchResult.getAddress());
                    blueToothName = searchResult.getName();
                    if (null == mac) {
                        mac = searchResult.getAddress();
                    } else {
                        if (isAutoConnect) {
                            if (mac.equals(searchResult.getAddress())) {
                                connectBle(mac);
                                return;
                            }
                        } else {
                            mac = searchResult.getAddress();
                            connectBle(mac);
                        }
                    }
                } else {
//                    scanBle();
                }
            }

            @Override
            public void onSearchCanceled() {
                BluetoothLog.d("取消扫描...");
            }
        });
    }

    private void connectBle(String mac) {
        bleClient.registerConnectStatusListener(mac, mBleConnectStatusListener);
//        BleConnectOptions options = new BleConnectOptions.Builder()
//                .setConnectRetry(3)   // 连接如果失败重试3次
//                .setConnectTimeout(30)   // 连接超时30s
//                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
//                .setServiceDiscoverTimeout(30)  // 发现服务超时20s
//                .build();

        bleClient.connect(mac, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) {
                if (code == REQUEST_SUCCESS) {
//                    List<BleGattService> services = profile.getServices();
                    ThreadPoolUtil.handler.removeCallbacks(autoConnectBle);
                    Map map = new LinkedHashMap();
                    map.put("msgId", 0);
                    map.put("destination", "appBle");
                    map.put("source", "waterSystem");
                    map.put("msgType", "event");
                    map.put("target", "bleConnectToWaterSystem");
                    Map value = new LinkedHashMap<>();
                    value.put("deviceUUID", WATERSYSTEM_SERVICE.toString());
                    value.put("rssi", maxRssi);
                    value.put("localName", blueToothName.replaceFirst("UNITO",""));
                    value.put("mac", mac);
                    value.put("unixTimestamp", System.currentTimeMillis());
                    value.put("deviceType", "Faucet");
                    map.put("value", value);
                    notity(JsonUtils.mapToJson(map));
                } else {
                    Map map = new LinkedHashMap();
                    map.put("msgId", 0);
                    map.put("destination", "appBle");
                    map.put("source", "waterSystem");
                    map.put("target", "bleConnectToWaterSystem");
                    map.put("msgType", "response");
                    map.put("response", "nack");
                    map.put("errorMessage", "systemAlreadyConnected");
                }
            }
        });
    }

    Runnable autoConnectBle = new Runnable() {
        @Override
        public void run() {
            Log.e("autoConnectBle-->","自动连接线程");
            search(true);
            ThreadPoolUtil.handler.postDelayed(autoConnectBle,3000);
        }
    };

    private void autoConnectBle() {

    }

    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String findMac, int status) {
            if (status == STATUS_CONNECTED) {
                disConnect = false;
                notifyDirData(mac);
            } else if (status == STATUS_DISCONNECTED) {
                ThreadPoolUtil.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Map map = new LinkedHashMap();
                        map.put("msgId", 0);
                        map.put("destination", "appBle");
                        map.put("source", "waterSystem");
                        map.put("target", "bleDisConnectToWaterSystem");
                        map.put("msgType", "event");
                        notity(JsonUtils.mapToJson(map));
                    }
                },500);
                if (!disConnect) {
                    ThreadPoolUtil.handler.postDelayed(autoConnectBle,3000);
                }
            }
        }
    };

    private void notifyDirData(String mac) {
        bleClient.notify(mac, WATERSYSTEM_SERVICE, WATERSYSTEM_CHARACTERSTIC, new BleNotifyResponse() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.e("readDirData--->", new String(value, StandardCharsets.UTF_8));
                ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut);

                if (null != value || value.length > 0) {
                    if (value[0] == (byte) 0x00 && value[value.length - 1] == (byte) 0xff) {
                        try {
                            ComConvertJson.CONVERT_MAP.get(value[3]).accept(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onResponse(int code) {

            }
        });
    }

    public void bleDisconnectFromWaterSystem() {
        disConnect = true;
        if (!isWrite) {
            isWrite = true;
        } else {
            msgId++;
        }
        if (getConnectStatus()) {
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("target", "bleDisconnectFromWaterSystem");
            map.put("msgType", "response");
            map.put("response", "ack");
            notity(JsonUtils.mapToJson(map));
            bleClient.disconnect(mac);
        } else {
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", "bleDisConnectFromWaterSystem");
            map.put("errorMessage", "systemAlreadyDisConnected");
            map.put("response", "nack");
            notity(JsonUtils.mapToJson(map));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void writeDirData(byte[] bytes) {
        if (getConnectStatus()) {
            if (ThreadPoolUtil.handler.hasCallbacks(runnableTimeOut)) {
                return;
            }
            if (!isWrite) {
                isWrite = true;
            } else {
                msgId++;
            }
            bleClient.write(mac, WATERSYSTEM_SERVICE, WATERSYSTEM_CHARACTERSTIC, bytes, new BleWriteResponse() {
                @Override
                public void onResponse(int code) {
                    if (code == REQUEST_SUCCESS) {
                        Log.e("writeDirData-->", "写入成功" + code);
                    }
                }
            });
            ThreadPoolUtil.handler.postDelayed(runnableTimeOut, timeOut);
        } else {
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", target);
            map.put("errorMessage", "disconnected");
            notity(JsonUtils.mapToJson(map));
        }
    }

    public void writeDirData2(byte[] bytes) {
        bleClient.write(mac, WATERSYSTEM_SERVICE, WATERSYSTEM_CHARACTERSTIC, bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {
                if (code == REQUEST_SUCCESS) {
                    Log.e("writeDirData-->", "写入成功" + code);
                }
            }
        });
    }

    static Runnable runnableTimeOut = new Runnable() {
        @Override
        public void run() {
            Log.e("timeOut--->","blueT TimeOut");
            Map map = new LinkedHashMap();
            map.put("msgId", msgId);
            map.put("destination", "appBle");
            map.put("source", "waterSystem");
            map.put("msgType", "response");
            map.put("target", UnitoManager.target);
            map.put("errorMessage", "timeOut");
            notity(JsonUtils.mapToJson(map));
        }
    };

    public static synchronized void notity(String jsonObject) {
        ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut);
        notifyResponse.unitoWaterSystemNotify(jsonObject);
    }

    public Map sendCommand(Map dataMap) {
        jsonMap = dataMap;
        if (null != JsonConvertCom.CONVERT_MAP.get(dataMap.get("target"))) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    JsonConvertCom.CONVERT_MAP.get(dataMap.get("target")).accept(dataMap);
                    if (jsonMap.get("msgType").equals("get") || jsonMap.get("msgType").equals("set")) {
                        UnitoManager.target = (String) dataMap.get("target");
                        if (jsonMap.containsKey("msgId")) {
                            if (isWrite) {
                                msgId++;
                                if (msgId == 10000) {
                                    msgId = 1;
                                }
                                isWrite = false;
                            }
                            jsonMap.put("msgId", msgId);
                        }
                    } else {
                        if (jsonMap.containsKey("msgId")) {
                            jsonMap.put("msgId", 0);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return jsonMap;
    }

    public static void appSettings(Map map) {

    }
}
