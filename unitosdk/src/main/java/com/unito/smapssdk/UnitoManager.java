package com.unito.smapssdk;

import static com.heaton.blelibrary.ble.model.BleDevice.TAG;
import static com.unito.smapssdk.library.BLEConstant.*;

import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.heaton.blelibrary.ble.Ble;
import com.heaton.blelibrary.ble.BleLog;
import com.heaton.blelibrary.ble.callback.BleConnectCallback;
import com.heaton.blelibrary.ble.callback.BleNotifyCallback;
import com.heaton.blelibrary.ble.callback.BleReadCallback;
import com.heaton.blelibrary.ble.callback.BleScanCallback;
import com.heaton.blelibrary.ble.callback.BleWriteCallback;
import com.heaton.blelibrary.ble.callback.BleWriteEntityCallback;
import com.heaton.blelibrary.ble.model.BleFactory;
import com.heaton.blelibrary.ble.utils.UuidUtils;
import com.unito.smapssdk.library.BLEConstant;
import com.unito.smapssdk.library.BleRssiDevice;
import com.unito.smapssdk.library.ComConvertJson;
import com.unito.smapssdk.library.JsonConvertCom;
import com.unito.smapssdk.library.JsonUtils;
import com.unito.smapssdk.library.LiveDataBus;
import com.unito.smapssdk.library.MyBleWrapperCallback;
import com.unito.smapssdk.library.NotifyResponse;
import com.unito.smapssdk.library.SocketUtil;
import com.unito.smapssdk.library.ThreadPoolUtil;
import com.unito.smapssdk.library.Utils;
import com.unito.smapssdk.library.UtilsKt;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UnitoManager {

    public static List<String> commandList = new ArrayList<>();
    private List<BleRssiDevice> saveSearchResult = new ArrayList<>();
    public String mac;
    public static int msgId = 1;
    public static String target = "";
    public static byte[] sendBytes;
    private static NotifyResponse notifyResponse;
    private int maxRssi;
    private String blueToothName;
    private Map jsonMap;
    public static boolean isWrite;
    private static Ble<BleRssiDevice> ble;
    BleRssiDevice bleRssiDevice;
    public int timeOut = 3000;
    private boolean disConnect = false;
    public static String secretKey = "YellowSubmarine_"; // Replace with your secret key

    private static final String SERVER_IP = "192.168.4.1"; // 服务端IP
    private static final int SERVER_PORT = 9008; // 服务端端口

    private volatile static UnitoManager singleton;
    public int isota; // 0,no 1,ws ota ing 2,ws ota end  6,esp ota ing 7,esp ota end
    private InputStream is;
    private InputStreamReader isr;
    private BufferedReader br;
    private String response;
    private SocketUtil socketUtil;
    private byte[] deviceUuid;
    public boolean isBoth;
    private int num = 0;

    private UnitoManager() {

    }

    public boolean getConnectStatus() {
        if (null != ble && ble.getConnectedDevices().size() > 0) {
            if (ble.getConnectedDevices().get(0).isConnected()) {
                return true;
            }
        }
        return false;
    }

    public boolean getBlueToothOpened() {
        if (null != ble) {
            if (ble.isBleEnable()) {
                return true;
            }
        }
        return false;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }


    public void registerUnitoWaterSystemNotify(Context context, NotifyResponse notifyResponse) {
        this.notifyResponse = notifyResponse;
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
        }, 500);
    }

    public static UnitoManager getSingleton() {
        if (singleton == null) {
            synchronized (UnitoManager.class) {
                if (singleton == null) {
                    singleton = new UnitoManager();
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

    //初始化蓝牙
    public static void initBle(Application mApplication) {
        Ble.options()
                .setLogBleEnable(true)//设置是否输出打印蓝牙日志
                .setThrowBleException(true)//设置是否抛出蓝牙异常
                .setLogTAG("AndroidBLE")//设置全局蓝牙操作日志TAG
                .setAutoConnect(false)//设置是否自动连接
                .setIgnoreRepeat(true)//设置是否过滤扫描到的设备(已扫描到的不会再次扫描)
                .setConnectFailedRetryCount(3)//连接异常时（如蓝牙协议栈错误）,重新连接次数
                .setConnectTimeout(10 * 1000)//设置连接超时时长
                .setScanPeriod(3 * 1000)//设置扫描时长
                .setMaxConnectNum(1)//最大连接数量
                .setUuidService(UUID.fromString(UuidUtils.uuid16To128("ffe0")))//设置主服务的uuid
                .setUuidWriteCha(UUID.fromString(UuidUtils.uuid16To128("ffe1")))//设置可写特征的uuid
                .setUuidReadCha(UUID.fromString(UuidUtils.uuid16To128("ffe1")))//设置可读特征的uuid （选填）
                .setUuidNotifyCha(UUID.fromString(UuidUtils.uuid16To128("ffe1")))//设置可通知特征的uuid （选填，库中默认已匹配可通知特征的uuid）
                .setFactory(new BleFactory<BleRssiDevice>() {//实现自定义BleDevice时必须设置
                    @Override
                    public BleRssiDevice create(String address, String name) {
                        return new BleRssiDevice(address, name);//自定义BleDevice的子类
                    }
                })
                .setBleWrapperCallback(new MyBleWrapperCallback())
                .create(mApplication, new Ble.InitCallback() {
                    @Override
                    public void success() {
                        BleLog.e("MainApplication", "初始化成功");
                    }

                    @Override
                    public void failed(int failedCode) {
                        BleLog.e("MainApplication", "初始化失败：" + failedCode);
                    }
                });
        ble = Ble.getInstance();
    }

    public void search(boolean isAutoConnect) {
        ble.startScan(new BleScanCallback<BleRssiDevice>() {
            @Override
            public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
                //Scanned devices
                device.setRssi(rssi);
                if (null != device.getBleName() && device.getBleName().contains("UNITO")) {
                    if (!saveSearchResult.contains(device)) {
                        saveSearchResult.add(device);
                    }
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                saveSearchResult.clear();
            }

            @Override
            public void onStop() {
                super.onStop();
                if (null != saveSearchResult && saveSearchResult.size() > 0) {
                    for (int i = 0; i < saveSearchResult.size(); i++) {
                        Log.e("saveSearchResult-->", saveSearchResult.get(i).getBleName() + "  " + saveSearchResult.get(i).getBleAddress() + "   " + saveSearchResult.get(i).getRssi());
                    }
                    maxRssi = -1000;
                    int position = -1;
                    for (int i = 0; i < saveSearchResult.size(); i++) {
                        if (saveSearchResult.get(i).getRssi() > maxRssi) {
                            maxRssi = saveSearchResult.get(i).getRssi();
                            position = i;
                        }
                    }
                    bleRssiDevice = saveSearchResult.get(position);
                    Log.e("maxRSSI-->", bleRssiDevice.getBleName() + "   " + bleRssiDevice.getBleAddress() + "   " + bleRssiDevice.getRssi());
//                    connectBle(searchResult.getAddress());
                    blueToothName = bleRssiDevice.getBleName();
//                    if (blueToothName.contains("40")) {
//                        WATERSYSTEM_SERVICE = HUB_SUCCESS_SERVICE;
//                        WATERSYSTEM_CHARACTERSTIC = HUB_SUCCESS_CHARACTERSTIC;
//                    }
                    if (null == mac) {
                        mac = bleRssiDevice.getBleAddress();
                    } else {
                        if (isAutoConnect) {
                            if (mac.equals(bleRssiDevice.getBleAddress())) {
//                                connectBle(mac);
                            }
                        } else {
                            mac = bleRssiDevice.getBleAddress();
//                            connectBle(mac);
                        }
                    }
                    connectBle(mac);
                } else {
//                    scanBle();
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e(TAG, "onScanFailed: " + errorCode);
            }
        });
        BleLog.e("searchBle--->", "aaaaaaaaa");
    }

    public void getBleDevices(ISearchBleCallback callback) {
        ble.startScan(new BleScanCallback<BleRssiDevice>() {
            @Override
            public void onLeScan(final BleRssiDevice device, int rssi, byte[] scanRecord) {
                //Scanned devices
                device.setRssi(rssi);
                if (null != device.getBleName() && device.getBleName().contains("UNITO")) {
                    if (!saveSearchResult.contains(device)) {
                        saveSearchResult.add(device);
                    }
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                saveSearchResult.clear();
            }

            @Override
            public void onStop() {
                super.onStop();
                if (null != saveSearchResult && saveSearchResult.size() > 0) {
                    for (int i = 0; i < saveSearchResult.size(); i++) {
                        Log.e("saveSearchResult-->", saveSearchResult.get(i).getBleName() + "  " + saveSearchResult.get(i).getBleAddress() + "   " + saveSearchResult.get(i).getRssi());
                    }

                }
                callback.onBleDeviceFind(saveSearchResult);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
                Log.e(TAG, "onScanFailed: " + errorCode);
                callback.onBleDeviceFind(saveSearchResult);
            }
        });
    }

    public interface ISearchBleCallback {
        public void onBleDeviceFind(List<BleRssiDevice> devices);
    }

    public void searchBleCallback(BleScanCallback<BleRssiDevice> bleScanCallback) {
        ble.startScan(bleScanCallback);
    }

    public interface IGetHubtokenCallback {
        public void onGetHubtoken(JSONObject jsonObject);
    }

    public void getHubtokenCallback(IGetHubtokenCallback iGetHubtokenCallback) {
        get40HubToken(iGetHubtokenCallback);
    }

    public void connectBle(String mac) {
        ble.connect(mac, connectCallback);
    }

    private BleConnectCallback<BleRssiDevice> connectCallback = new BleConnectCallback<BleRssiDevice>() {
        @Override
        public void onConnectionChanged(BleRssiDevice device) {
            Log.e(TAG, "onConnectionChanged: " + device.getConnectionState() + Thread.currentThread().getName());
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
            value.put("localName", blueToothName.replaceFirst("UNITO", ""));
            value.put("mac", mac);
            value.put("unixTimestamp", System.currentTimeMillis());
            value.put("deviceType", "Faucet");
            map.put("value", value);
            notity(JsonUtils.mapToJson(map));
        }

        @Override
        public void onConnectFailed(BleRssiDevice device, int errorCode) {
            super.onConnectFailed(device, errorCode);
            BleLog.e(TAG, "---onConnectFailed");
            ble.disconnectAll();
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
            }, 500);
//            if (!disConnect) {
//                ThreadPoolUtil.handler.postDelayed(autoConnectBle, 3000);
//            }
        }

        @Override
        public void onConnectCancel(BleRssiDevice device) {
            super.onConnectCancel(device);
            Log.e(TAG, "onConnectCancel: " + device.getBleName());
        }

        @Override
        public void onServicesDiscovered(BleRssiDevice device, BluetoothGatt gatt) {
            super.onServicesDiscovered(device, gatt);
        }

        @Override
        public void onReady(BleRssiDevice device) {
            super.onReady(device);
            //连接成功后，设置通知
            ble.enableNotify(device, true, new BleNotifyCallback<BleRssiDevice>() {
                @Override
                public void onChanged(BleRssiDevice device, BluetoothGattCharacteristic characteristic) {
                    ThreadPoolUtil.handler.removeCallbacks(runnableTimeOut);
                    if (null != characteristic.getValue() || characteristic.getValue().length > 0) {
                        if (characteristic.getValue()[0] == (byte) 0x00 && characteristic.getValue()[characteristic.getValue().length - 1] == (byte) 0xff) {
                            try {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    ThreadPoolUtil.handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                ComConvertJson.CONVERT_MAP.get(characteristic.getValue()[3]).accept(characteristic.getValue());
                                            } catch (Exception exception) {
                                                exception.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (characteristic.getValue()[0] == (byte) 0x55 && characteristic.getValue()[1] == (byte) 0x08) {
                            if (isota == 1) {
                            } else {
                                isota = 1;
                            }
                            num++;
                            writeOtaData(Utils.hexStringToByteArray(commandList.get(num)));
                        } else if (characteristic.getValue()[0] == (byte) 0x55 && characteristic.getValue()[1] == (byte) 0x03 && characteristic.getValue()[4] == (byte) 0x00 && characteristic.getValue()[5] == (byte) 0x00 && characteristic.getValue()[7] == (byte) 0x10 && characteristic.getValue()[characteristic.getValue().length - 1] == (byte) 0x01) {
                            num++;
                            writeOtaData(Utils.hexStringToByteArray(commandList.get(num)));
                        } else if (characteristic.getValue()[0] == (byte) 0x55 && characteristic.getValue()[1] == (byte) 0x03 && characteristic.getValue()[4] == (byte) 0x00 && characteristic.getValue()[5] == (byte) 0x00 && characteristic.getValue()[7] == (byte) 0x88 && characteristic.getValue()[characteristic.getValue().length - 1] == (byte) 0x01) {
                            num++;
                            writeOtaData(Utils.hexStringToByteArray(commandList.get(num)));
                        } else if (characteristic.getValue()[0] == (byte) 0x55 && characteristic.getValue()[1] == (byte) 0x02) {
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            num++;
                            writeOtaData(Utils.hexStringToByteArray(commandList.get(num)));
                        } else if (characteristic.getValue()[0] == (byte) 0x55 && characteristic.getValue()[1] == (byte) 0x09) {
                            num = 0;
                            Log.e("", "ota完成");
                        }
                    }
                }

                @Override
                public void onNotifySuccess(BleRssiDevice device) {
                    super.onNotifySuccess(device);
                    BleLog.e(TAG, "onNotifySuccess: " + device.getBleName());
                }
            });
        }
    };


    Runnable autoConnectBle = new Runnable() {
        @Override
        public void run() {
            Log.e("autoConnectBle-->", "自动连接线程");
//            search(false);
            ThreadPoolUtil.handler.postDelayed(autoConnectBle, 3000);
        }
    };

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
//            bleClient.disconnect(mac);
            ble.disconnectAll();
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

    public void get40HubToken(IGetHubtokenCallback callback) {
        ble.readByUuid(bleRssiDevice, HUB_SUCCESS_SERVICE, HUB_SUCCESS_CHARACTERSTIC, new BleReadCallback<BleRssiDevice>() {
            @Override
            public void onReadSuccess(BleRssiDevice dedvice, BluetoothGattCharacteristic characteristic) {
                super.onReadSuccess(dedvice, characteristic);
                final byte[] bytes = characteristic.getValue();
                String response = null;
                try {
                    response = new String(UtilsKt.decryptWithAES(secretKey, bytes), StandardCharsets.UTF_8);
                    final JSONObject jsonObject = new JSONObject(response);
                    BleLog.e("token--->", jsonObject.toString());
                    callback.onGetHubtoken(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onReadFailed(BleRssiDevice device, int failedCode) {
                super.onReadFailed(device, failedCode);
                callback.onGetHubtoken(null);
            }
        });
    }

    public int writeDirData(byte[] bytes) {
        if (getConnectStatus()) {
            if (!isWrite) {
                isWrite = true;
            } else {
                msgId++;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ThreadPoolUtil.handler.hasCallbacks(runnableTimeOut)) {
                    return msgId;
                }
            }
            ble.write(bleRssiDevice, bytes, new BleWriteCallback<BleRssiDevice>() {
                @Override
                public void onWriteSuccess(BleRssiDevice device, BluetoothGattCharacteristic characteristic) {
                    BleLog.e("onWriteSuccess: ", "success");
                }

                @Override
                public void onWriteFailed(BleRssiDevice device, int failedCode) {
                    super.onWriteFailed(device, failedCode);
                    BleLog.e("onWriteFailed: ", "failed");
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
        return msgId;
    }

    public void writeOtaData(byte[] bytes) {
        ble.writeEntity(bleRssiDevice, bytes, 20, 50, new BleWriteEntityCallback<BleRssiDevice>() {

            @Override
            public void onWriteSuccess() {
                BleLog.e("onWriteSuccess: ", "success->" + Utils.bytesToHex(bytes));
            }

            @Override
            public void onWriteFailed() {
//                BleLog.e("onWriteFailed: ", "failed->" + Utils.bytesToHex(bytes));
            }
        });
    }

    Runnable runnableTimeOut = new Runnable() {
        @Override
        public void run() {
            Log.e("timeOut--->", "blueT TimeOut");
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

    public synchronized void notity(String jsonObject) {
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

//---------------------------System Identification----------------------//

    /**
     * Request for water system identification
     *
     * @return
     */
    public void setRequestForWaterSystemIdentification() {

        //For Direct
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00;
        bytes[1] = getDestinationAddressForIdentification();
        bytes[2] = getSourceAddress();
        bytes[3] = MSGID_WATER_SYSTEM_IDENTIFICATION1;
        bytes[4] = MSGID_WATER_SYSTEM_IDENTIFICATION2;
        bytes[5] = GET;
        bytes[6] = (byte) 0x01;
        bytes[7] = (byte) 0x01;
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }

        //For Hub
        //        byte[] bytes = new byte[10];
        //        bytes[0] = 0x00;
        //        bytes[1] = 0x0b;
        //        bytes[2] = 0x07;
        //        bytes[3] = (byte) 0xA0;
        //        bytes[4] = 0x01;
        //        bytes[5] = 0x01;
        //        bytes[6] = 0x01;
        //        bytes[7] = 0x01;
        //        bytes[8] = (byte) Utils.getCheckSum(bytes);
        //        bytes[9] = (byte) 0xFF;
        //        return bytes;

    }

    //---------------------------Water System Status----------------------//

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public void setRequestForSodaWaterParameters(int sodaWaterInMinTime, int sodaWaterInMaxTime, int sodaWaterInTemperature) {
        byte[] bytes = new byte[12];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_WATER_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_WATER_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x03;//DATA LENGTH
        bytes[7] = (byte) sodaWaterInMinTime;//VAL
        bytes[8] = (byte) sodaWaterInMaxTime;//VAL
        bytes[9] = (byte) sodaWaterInTemperature;//VAL
        bytes[10] = (byte) Utils.getCheckSum(bytes);
        bytes[11] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public void getRequestForSodaWaterParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_WATER_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_WATER_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public void getRequestForGetError() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_ERROR;//MSGID High
        bytes[4] = MSGID_ERROR2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public void setRequestForSodaCo2InParameters(int co2InMinTime, int co2InMaxTime, int co2SoftLevelTime, int co2MediumLevelTime, int co2IntenseLevelTime) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_CO2_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x05;//DATA LENGTH
        bytes[7] = (byte) co2InMinTime;//VAL
        bytes[8] = (byte) co2InMaxTime;//VAL
        bytes[9] = (byte) co2SoftLevelTime;//VAL
        bytes[10] = (byte) co2MediumLevelTime;//VAL
        bytes[11] = (byte) co2IntenseLevelTime;//VAL
        bytes[12] = (byte) Utils.getCheckSum(bytes);
        bytes[13] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA WATER PARAMETERS
     *
     * @return
     */
    public void getRequestForSodaSodaCo2InParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_CO2_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public void setRequestForSodaCo2InOnOffParameters(int co2InOnOffStatus, int co2InOnNominalTime, int co2InOnMinTime, int co2InOnMaxTime, int co2InOffNominalTime, int co2InOffMinTime, int co2InOffMaxTime) {
        byte[] bytes = new byte[16];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_CO2_INONOFF_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_INONOFF_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x07;//DATA LENGTH
        bytes[7] = (byte) co2InOnOffStatus;//VAL
        bytes[8] = (byte) co2InOnNominalTime;//VAL
        bytes[9] = (byte) co2InOnMinTime;//VAL
        bytes[10] = (byte) co2InOnMaxTime;//VAL
        bytes[11] = (byte) co2InOffNominalTime;//VAL
        bytes[12] = (byte) co2InOffMinTime;//VAL
        bytes[13] = (byte) co2InOffMaxTime;//VAL
        bytes[14] = (byte) Utils.getCheckSum(bytes);
        bytes[15] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public void getRequestForSodaCo2InOnOffParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_SODA_CO2_INONOFF_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_SODA_CO2_INONOFF_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of watersystem dashboard
     *
     * @return
     */
    public void setRequestForWaterSystemData() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_REQUEST_STATUS_WATER_SYSTEM1;//MSGID High
        bytes[4] = MSGID_REQUEST_STATUS_WATER_SYSTEM2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public void setRequestForWashPipeParameters(int boilingWaterFlushTime, int sparkleWaterFlushTime) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WASH_PIPE_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_WASH_PIPE_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x02;//DATA LENGTH
        bytes[7] = (byte) boilingWaterFlushTime;//VAL
        bytes[8] = (byte) sparkleWaterFlushTime;//VAL
        bytes[9] = (byte) Utils.getCheckSum(bytes);
        bytes[10] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public void getRequestForWashPipeParameters() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WASH_PIPE_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_WASH_PIPE_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public void setRequestForWateroutStatus(int waterType) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_OUT_STATUS_1;//MSGID High
        bytes[4] = MSGID_WATER_OUT_STATUS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) waterType;//VAL=
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public void getRequestForWaterOutStatus(int waterType) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_OUT_STATUS_1;//MSGID High
        bytes[4] = MSGID_WATER_OUT_STATUS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) waterType;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    /**
     * Request for gettting value of SODA co2 0ff PARAMETERS
     *
     * @return
     */
    public void setRequestForPowerOn(int systemPowerOn) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_POWER_ON_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_POWER_ON_PARAMETERS_2;//MSGID LOW
        bytes[5] = SET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) systemPowerOn;//VAL
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public void getRequestForPowerOn(int systemPowerOn) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_POWER_ON_PARAMETERS_1;//MSGID High
        bytes[4] = MSGID_POWER_ON_PARAMETERS_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) systemPowerOn;//DATA LENGTH
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for gettting value of SODA co2 off PARAMETERS
     *
     * @return
     */
    public void getRequestForWaterType() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_TYPE_1;//MSGID High
        bytes[4] = MSGID_WATER_TYPE_2;//MSGID LOW
        bytes[5] = GET;//MSG CATEGORY
        bytes[6] = (byte) 0x01;//DATA LENGTH
        bytes[7] = (byte) 0x01;//DATA LENGTH
        bytes[8] = (byte) Utils.getCheckSum(bytes);
        bytes[9] = (byte) 0xFF;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForFlavor() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = 0x0B; //Destination
        bytes[2] = 0X07; //Source
        bytes[3] = (byte) 0XEE; //MSGID High
        bytes[4] = (byte) 0XFE; //MSGID LOW
        bytes[5] = 0X01; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //---------------------------HOT Water System Status----------------------//

    /**
     * Request for gettting value of hot water system
     *
     * @return
     */
    public void getRequestForHotWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_HOTWATER1; //MSGID High
        bytes[4] = MSGID_GET_HOTWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("getRequestForHotWater", "SET HEX = " + Utils.bytesToHex(bytes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for set value of hot water system
     */
    public void setRequestForHotWater(int desiretemp, int hysteresistemp) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_HOTWATER1; //MSGID High
        bytes[4] = MSGID_GET_HOTWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) desiretemp; //Desire temp
        bytes[8] = (byte) hysteresistemp; //Hysteresis temp
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        Log.d("setRequestForHotWater", "GET HEX = " + Utils.bytesToHex(bytes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //---------------------------Cold Water System Status----------------------//

    /**
     * Request for gettting value of Cold water system
     *
     * @return
     */
    public void getRequestForColdWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_COLDWATER1; //MSGID High
        bytes[4] = MSGID_GET_COLDWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("getRequestForColdWater", "GET HEX = " + Utils.bytesToHex(bytes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    /**
     * Request for set value of Cold water system
     *
     * @return
     */
    public void setRequestForColdWater(int desiretemp, int hysteresistemp) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_COLDWATER1; //MSGID High
        bytes[4] = MSGID_GET_COLDWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) desiretemp; //Desire temp
        bytes[8] = (byte) hysteresistemp; //Hysteresis temp
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        Log.d("getRequestForColdWater", "GET HEX = " + Utils.bytesToHex(bytes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //---------------------------Soda Water System Status----------------------//

    /**
     * Request for gettting value of Soad water system
     *
     * @return
     */
    public void getRequestForSodaWater() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_SODAWATER1; //MSGID High
        bytes[4] = MSGID_GET_SODAWATER2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for set value of Soda Water system
     *
     * @return
     */
    public void setRequestForSodaWater(int val) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_SODAWATER1; //MSGID High
        bytes[4] = MSGID_GET_SODAWATER2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) val; //Desire temp
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //---------------------------CHILD PROTECT---------------------//

    /**
     * Request for get child protection data
     *
     * @return
     */

    public void getRequestForChildProtect() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CHILD_PROTECT1; //MSGID High
        bytes[4] = MSGID_CHILD_PROTECT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for set child protection
     *
     * @param isProtect
     * @return
     */
    public void setRequestForChildProtect(int isProtect) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CHILD_PROTECT1; //MSGID High
        bytes[4] = MSGID_CHILD_PROTECT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isProtect; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }
    //---------------------------GET WATER SYSTEM CLOCK---------------------//

    public void getRequestForWaterSystemClock() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_SYSTEM_CLOCK1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_CLOCK2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForWaterSystemClock(int year, int month, int date, int day, int hour, int min, int sec) {
        byte[] bytes = new byte[16];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_SYSTEM_CLOCK1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_CLOCK2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x07; //DATA LENGTH
        bytes[7] = (byte) year; //YEAR
        bytes[8] = (byte) month; //MONTH
        bytes[9] = (byte) day; //DAY
        bytes[10] = (byte) date; //DATE
        bytes[11] = (byte) hour; //HOUR
        bytes[12] = (byte) min; //MIN
        bytes[13] = (byte) sec; //SEC
        bytes[14] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[15] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //-------------------------Water Filter-------------------------
    public void getRequestForWaterFilter() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FILTER_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_FILTER_REPLACEMENT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public void setRequestForFilterReplacement(int filter) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FILTER_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_FILTER_REPLACEMENT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) filter; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public void setRequestForActivateFlavorDisinfection(int activateFlavorDisinfection) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_ACTIVATE_FLAVOR_DISINFECTION; //MSGID High
        bytes[4] = MSGID_ACTIVATE_FLAVOR_DISINFECTION2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) activateFlavorDisinfection; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    //-------------------------C02 Tank Replacement-------------------------
    public void getRequestForTankReplacement() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CO2_TANK_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_CO2_TANK_REPLACEMENT2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public void setRequestForTankReplacement(int filter) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CO2_TANK_REPLACEMENT1; //MSGID High
        bytes[4] = MSGID_CO2_TANK_REPLACEMENT2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) filter; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }


    }

    //---------------------------Check Message---------------------//
    public boolean checkMessage(byte[] val, byte msgidHigh, byte msgidlow) {
        if (val[0] != (byte) 0x00) {
            return false;
        } else if (val[val.length - 1] != (byte) 0xFF) {
            return false;
        } else if (val.length - 9 != val[6]) {
            return false;
        } else if (Utils.getCheckSum(val) != val[val.length - 2]) {
            return false;
        } else if (val[3] != msgidHigh) {
            return false;
        } else return val[4] == msgidlow;
    }

    //-----------------Timer--------------------------

    public void getRequestForTimer1() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_TIMER1_1; //MSGID High
        bytes[4] = MSGID_TIMER1_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForTimer2() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_TIMER2_1; //MSGID High
        bytes[4] = MSGID_TIMER2_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Set request for filter replacement
     *
     * @return
     */
    public void setRequestForTimer1(int[] timerarray) {
        byte[] bytes = new byte[18];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_TIMER1_1; //MSGID High
        bytes[4] = MSGID_TIMER1_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x09; //DATA LENGTH

        bytes[7] = (byte) timerarray[0]; //DATA DAYS
        bytes[8] = (byte) timerarray[1]; //DATA FROM TIME1
        bytes[9] = (byte) timerarray[2]; //DATA TO TIME1
        bytes[10] = (byte) timerarray[3]; //DATA FROM TIME2
        bytes[11] = (byte) timerarray[4]; //DATA TO TIME2
        bytes[12] = (byte) timerarray[5]; //DATA FROM TIME3
        bytes[13] = (byte) timerarray[6]; //DATA TO TIME3
        bytes[14] = (byte) timerarray[7]; //DATA FROM TIME4
        bytes[15] = (byte) timerarray[8]; //DATA TO TIME4

        bytes[16] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[17] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    public void setRequestForTimer2(int[] timerarray) {
        byte[] bytes = new byte[18];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_TIMER2_1; //MSGID High
        bytes[4] = MSGID_TIMER2_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x09; //DATA LENGTH

        bytes[7] = (byte) timerarray[0]; //DATA DAYS
        bytes[8] = (byte) timerarray[1]; //DATA FROM TIME1
        bytes[9] = (byte) timerarray[2]; //DATA TO TIME1
        bytes[10] = (byte) timerarray[3]; //DATA FROM TIME2
        bytes[11] = (byte) timerarray[4]; //DATA TO TIME2
        bytes[12] = (byte) timerarray[5]; //DATA FROM TIME3
        bytes[13] = (byte) timerarray[6]; //DATA TO TIME3
        bytes[14] = (byte) timerarray[7]; //DATA FROM TIME4
        bytes[15] = (byte) timerarray[8]; //DATA TO TIME4

        bytes[16] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[17] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //------------------------------Sterialization----------------------------------------------------------
    public void getRequestForSterialization() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_STERILIZATION_1; //MSGID High
        bytes[4] = MSGID_STERILIZATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForSterialization(int time, int days) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_STERILIZATION_1; //MSGID High
        bytes[4] = MSGID_STERILIZATION_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) days; //DATA DAYS
        bytes[8] = (byte) time; //DATA TIME
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //------------------------------Consumable----------------------------------------------------------

    public void setRequestForConsumable(int type, int val, byte setOrGet) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CONSUMABLE_1; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2; //MSGID LOW
        bytes[5] = setOrGet; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) type; //SYSTEM TYPE
        bytes[8] = (byte) val; //SYSTEM TYPE
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForConsumableNew() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CONSUMABLE_1_NEW; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2_NEW; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //SYSTEM TYPE
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForConsumableNew(int replacementId, int replacementType, int quantity, int expirationInDays) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_CONSUMABLE_1_NEW; //MSGID High
        bytes[4] = MSGID_CONSUMABLE_2_NEW; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x05; //DATA LENGTH
        bytes[7] = (byte) replacementId; // replacementId
        bytes[8] = (byte) replacementType; // replacementType
        bytes[9] = (byte) quantity; // quantity
        bytes[10] = (byte) (expirationInDays % 0xff); // L
        bytes[11] = (byte) (expirationInDays / 0xff); // H
        bytes[12] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[13] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //------------------------------Communication----------------------------------------------------------

    public void getRequestForHubCommunication() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_COMMUNICATION_1; //MSGID High
        bytes[4] = MSGID_COMMUNICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForHubProvisoingMode() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_COMMUNICATION_1; //MSGID High
        bytes[4] = MSGID_COMMUNICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //------------------------------Diagnostic----------------------------------------------------------
    public void getRequestForDiagnostic() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_DIAGNOSTIC_1; //MSGID High
        bytes[4] = MSGID_DIAGNOSTIC_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    //---------------------------------UVLAMP--------------------------------------------------

    /**
     * Request for check UVLamp exits
     *
     * @return
     */
    public void getRequestForUVLamp() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress();
        //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_UVLAMP_1; //MSGID High
        bytes[4] = MSGID_UVLAMP_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Request for set UVLamp Status
     *
     * @param isexits
     * @return
     */
    public void setRequestForForUVLamp(int isexits) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddress();
        //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_UVLAMP_1; //MSGID High
        bytes[4] = MSGID_UVLAMP_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isexits; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    // Hub Version
    public void getRequestForHubVersion() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First

        //        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, BLEConstant.APP_HUB) == BLEConstant.APP_HUB) {
        //            bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        //            bytes[4] = MSGID_HUB_VERSION_2;
        //            bytes[1] = APP_HUB; //Destination
        //            bytes[2] = getSourceAddress(); //Source
        //        } else {
        //            bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        //            bytes[4] = MSGID_HUB_VERSION_2;
        //            bytes[1] = getDestinationAddress(); //Destination
        //            bytes[2] = getSourceAddress(); //Source
        //        }

        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_VERSION_1; //MSGID High
        bytes[4] = MSGID_HUB_VERSION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    // Version
    public void getRequestVersion(int boardType) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = VERSION_1; //MSGID High
        bytes[4] = VERSION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) boardType; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    // Get Request WaterSystem Uptime
    public void getWaterSystemUptime() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_WATER_SYSTEM_UPTIME_1; //MSGID High
        bytes[4] = MSGID_WATER_SYSTEM_UPTIME_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForHubMacAddress() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MAC_ADDRESS_1; //MSGID High
        bytes[4] = MSGID_HUB_MAC_ADDRESS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForHubIPAddress() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_IP_ADDRESS_1; //MSGID High
        bytes[4] = MSGID_HUB_IP_ADDRESS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForDIRNotification() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_DIR_NOTIFICATION_1; //MSGID High
        bytes[4] = MSGID_DIR_NOTIFICATION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public byte[] setRequestForDirModeFlashProcess(int startFlash) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_DIR_MODE_FLASH_PROCESS_1; //MSGID High
        bytes[4] = MSGID_DIR_MODE_FLASH_PROCESS_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) startFlash; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public static byte[] getRequestForHUBModeFlashProcess() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = APP_HUB; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_DIR_MODE_FLASH_PROCESS_1; //MSGID High
        bytes[4] = MSGID_DIR_MODE_FLASH_PROCESS_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    public void setRequestForHUBModeOTAForHUB() {
        //        00 0b 06 04 fe 02 01 02
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddressForIdentification(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = 0x04; //MSGID High
        bytes[4] = (byte) 0xfe; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForHUBModeOTAForWS() {
        //        00 0b 06 04 fe 02 01 01
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = getDestinationAddressForIdentification(); //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForPairing(byte valueToBeSet) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_PAIRING_1; //MSGID High
        bytes[4] = MSGID_PAIRING_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) valueToBeSet; //DATA
        bytes[8] = (byte) valueToBeSet; //DATA
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForPairing() {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_PAIRING_1; //MSGID High
        bytes[4] = MSGID_PAIRING_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForWSOTAEnabled() {
        //        00 01 07 04 FE 01 01 01 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForHubOTAEnabled() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_HUB_MODE_WS_OTA_1; //MSGID High
        bytes[4] = MSGID_HUB_MODE_WS_OTA_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    /**
     * Get the source address  weather BLE is connected via INTERNAL(HUB,DIR) or EXTERNAL(CLOUD)
     *
     * @return
     */
    public byte getSourceAddress() {
//        if (Unito.getInstance().getSharedPreferences().getInt(PREF.SOURCE_ADDRESS, APP_INTERNAL) == APP_INTERNAL) {
//            return APP_INTERNAL;
//        } else if (Unito.getInstance().getSharedPreferences().getInt(PREF.SOURCE_ADDRESS, APP_INTERNAL) == APP_EXTERNAL) {
//            return APP_EXTERNAL;
//        }
        return APP_INTERNAL;
    }

    /**
     * Get the destination address  weather BLE is connected via DIR,HUB or CLOUD
     *
     * @return
     */
    public byte getDestinationAddress() {
//        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS, WATER_SYSTEM_CONTROLLER) == COOKER_SYSTEM_CONTROLLER) {
//            return COOKER_SYSTEM_CONTROLLER;
//        } else if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS, WATER_SYSTEM_CONTROLLER) == HOOD_SYSTEM_CONTROLLER) {
//            return HOOD_SYSTEM_CONTROLLER;
//        } else {
//            return WATER_SYSTEM_CONTROLLER;
//        }
        return 0;
    }

    public byte getDestinationAddressForIdentification() {
//        if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, APP_DIRECT) == APP_CLOUD) {
//            return APP_CLOUD;
//        } else if (Unito.getInstance().getSharedPreferences().getInt(PREF.DESTINATION_ADDRESS_FOR_IDENTIFICATION, APP_DIRECT) == APP_HUB) {
//            return APP_HUB;
//        } else {
//            return APP_DIRECT;
//        }
        return 0;
    }


    /**
     * Request for set ENABLE DISABLE Pull out sensor
     *
     * @param isEnablePullOutSensor
     * @return
     */

    /*D (1043386) GATTS: Received message from App:
I (1043390) GATTS: 00 0b 07 3e 0c 01 01 02 a0 ff
D (1043394) GATTS: Routing incoming message.
E (1043398) ROUTE: Unsupported SMAPS message received id = 3134, ignored.*/
    public void setRequestForEnablePullOutSensor(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    public void getRequestForEnablePullOutSensor() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_PULL_OUT_SENSOR_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForAutoSodaReFill() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_AUTO_SODA_REFILL_1; //MSGID High
        bytes[4] = MSGID_GET_AUTO_SODA_REFILL_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForAutoSodaReFill(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_AUTO_SODA_REFILL_1; //MSGID High
        bytes[4] = MSGID_GET_AUTO_SODA_REFILL_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForAutoFillTimer() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_AUTO_FILL_TIMER_1; //MSGID High
        bytes[4] = MSGID_AUTO_FILL_TIMER_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForAutoFillTimer(int timerValue) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_AUTO_FILL_TIMER_1; //MSGID High
        bytes[4] = MSGID_AUTO_FILL_TIMER_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) timerValue; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    public void getRequestForFlushTimerBoiling() {
        //[0, 7, 1, 50, 12, 4, 2, 15, 14, -105, -1]
        //00 07 01 32 0C 04 02 0F 0E 97 FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLUSH_TIMER_1; //MSGID High
        bytes[4] = MSGID_FLUSH_TIMER_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForFlushTimerBoiling(int timerBoiling, int timerSparkle) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLUSH_TIMER_1; //MSGID High
        bytes[4] = MSGID_FLUSH_TIMER_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) timerBoiling; //timerBoiling
        bytes[8] = (byte) timerSparkle; //timerSparkle
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForFlavorParameters() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForFlavorParametersCleaning() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_CLEANING_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_CLEANING_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForFlavorParametersDisinfection() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        /*bytes[1] = getDestinationAddressForIdentification(); //Destination*/
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForFlavorParameters(int flavorIntense, int flavorMedium, int flavorSoft, int waterPerInjection, int sodaPerInjection) {
        byte[] bytes = new byte[14];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x05; //DATA LENGTH
        bytes[7] = (byte) flavorIntense; //VALUE FOR flavorInjection
        bytes[8] = (byte) flavorMedium; //VALUE FOR flavorMedium
        bytes[9] = (byte) flavorSoft; //VALUE FOR flavorIntense
        bytes[10] = (byte) waterPerInjection; //VALUE FOR waterPerInjection
        bytes[11] = (byte) sodaPerInjection; //VALUE FOR sodaPerInjection
        bytes[12] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[13] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForFlavorParametersCleaning(int durationSecond, int durationHours) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_CLEANING_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_CLEANING_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) durationSecond; //VALUE FOR flavorInjection
        bytes[8] = (byte) durationHours; //VALUE FOR flavorMedium
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForFlavorParametersDisinfection(int fisinfectionCycles, int disinfectionForward, int disinfectionBackwards,
                                                          int disinfectionThresholdLow, int disinfectionThresholdHigh, int disinfectionThreshold) {
        byte[] bytes = new byte[15];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_PARAMETERS_DISINFECTION_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x06; //DATA LENGTH
        bytes[7] = (byte) fisinfectionCycles;
        bytes[8] = (byte) disinfectionForward;
        bytes[9] = (byte) disinfectionBackwards;
        bytes[10] = (byte) disinfectionThresholdLow;
        bytes[11] = (byte) disinfectionThresholdHigh;
        bytes[12] = (byte) disinfectionThreshold;
        bytes[13] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[14] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    public void getRequestForFlavorWater() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_WATER_TYPE_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_WATER_TYPE_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setRequestForFlavorWater(int flavorType) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_FLAVOR_WATER_TYPE_1; //MSGID High
        bytes[4] = MSGID_FLAVOR_WATER_TYPE_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) flavorType; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForEnableLeakageSensor() {
        //        00 01 07 04 FE 01 01 02 CS FF
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x01; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }


    public void setRequestForEnableLeakageSensor(int isEnablePullOutSensor) {
        byte[] bytes = new byte[10];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_1; //MSGID High
        bytes[4] = MSGID_GET_ENABLE_LEAKAGE_SENSOR_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) isEnablePullOutSensor; //VALUE FOR LOCK/UNLOCK
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        Log.d("Indianic BLE CO", "SET HEX = " + Utils.bytesToHexLog(bytes));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void getRequestForSingleClick() {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_SINGLE_CLICK_1; //MSGID High
        bytes[4] = MSGID_GET_SINGLE_CLICK_2; //MSGID LOW
        bytes[5] = GET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = (byte) 0x02; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public void setSingleClick(int resolution, int enableDisable) {
        byte[] bytes = new byte[11];

        bytes[0] = (byte) 0x00; //First
        bytes[1] = WATER_SYSTEM_CONTROLLER; //Destination
        bytes[2] = APP_INTERNAL; //Source
        bytes[3] = MSGID_GET_SINGLE_CLICK_1; //MSGID High
        bytes[4] = MSGID_GET_SINGLE_CLICK_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x02; //DATA LENGTH
        bytes[7] = (byte) enableDisable; //resolution
        bytes[8] = (byte) resolution; //enableDisable
        bytes[9] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[10] = (byte) 0xFF; //LAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            writeDirData(bytes);
        }
    }

    public byte[] Turn_on_wifi(byte type, byte[] uuid) {
        byte[] bytes = new byte[15];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = BLEConstant.APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = START_ESP_OTA_1; //MSGID High
        bytes[4] = START_ESP_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x06; //DATA LENGTH
        bytes[7] = 0x01; //DATA
        bytes[8] = type; //DATA
        bytes[9] = (byte) uuid[0]; //DATA
        bytes[10] = (byte) uuid[1]; //DATA
        bytes[11] = (byte) uuid[2]; //DATA
        bytes[12] = (byte) uuid[3]; //DATA
        bytes[13] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[14] = (byte) 0xFF; //LAST

        return bytes;
    }

    public byte[] selectType(byte type, byte[] uuid) {
        byte[] bytes = new byte[19];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = BLEConstant.APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = START_ESP_OTA_1; //MSGID High
        bytes[4] = START_ESP_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x0a; //DATA LENGTH
        bytes[7] = 0x01; //DATA
        bytes[8] = type; //DATA
        bytes[9] = (byte) uuid[0]; //DATA
        bytes[10] = (byte) uuid[1]; //DATA
        bytes[11] = (byte) uuid[2]; //DATA
        bytes[12] = (byte) uuid[3]; //DATA
        bytes[13] = (byte) 0x00; //DATA
        bytes[14] = (byte) 0x00; //DATA
        bytes[15] = (byte) 0x00; //DATA
        bytes[16] = (byte) 0x00; //DATA
        bytes[17] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[18] = (byte) 0xFF; //LAST
        return bytes;
    }


    public byte[] setESPOTA2(byte data, byte num1, byte num2) {
        byte[] bytes = new byte[12];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = BLEConstant.APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = START_ESP_OTA_1; //MSGID High
        bytes[4] = START_ESP_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x03; //DATA LENGTH
        bytes[7] = data; //DATA
        bytes[8] = num1; //DATA
        bytes[9] = num2; //DATA
        bytes[10] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[11] = (byte) 0xFF; //LAST
        return bytes;
    }

    public byte[] setESPOTA5(byte data) {
        byte[] bytes = new byte[10];
        bytes[0] = (byte) 0x00; //First
        bytes[1] = BLEConstant.APP_HUB; //Destination
        bytes[2] = getSourceAddress(); //Source
        bytes[3] = START_ESP_OTA_1; //MSGID High
        bytes[4] = START_ESP_OTA_2; //MSGID LOW
        bytes[5] = SET; //MSG CATEGORY
        bytes[6] = (byte) 0x01; //DATA LENGTH
        bytes[7] = data; //DATA
        bytes[8] = (byte) Utils.getCheckSum(bytes); // CHECKSUM
        bytes[9] = (byte) 0xFF; //LAST
        return bytes;
    }

    private void getWS20File() {
        try {
            commandList = Utils.txtToArrayList("/data/data/com.unito.smapstestapp/files/1.txt");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "ota文件不存在");
        }
    }

    private void getWSFile() {
        try {
            commandList = Utils.txtToArrayList("/data/data/com.unito.smapstestapp/files/3.txt");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("", "ota文件不存在");
        }
    }

    public void starOTA() {
        writeDirData(setRequestForDirModeFlashProcess(1));
        getWS20File();
        ThreadPoolUtil.handler.postDelayed(() -> writeOtaData(Utils.hexStringToByteArray(commandList.get(num))), 10000);
    }

    public void socketWriteData(byte[] bytes) {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                socketUtil.write(bytes);
            }
        });
    }

    public void connectHotPort() {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                socketUtil = new SocketUtil(SERVER_IP, SERVER_PORT);
                socketUtil.run(deviceUuid);
            }
        });
    }

    public void openHotPort(byte[] deviceUuid) {
        this.deviceUuid = deviceUuid;
        UnitoManager.getSingleton().writeDirData(UnitoManager.getSingleton().Turn_on_wifi((byte) 0x09, deviceUuid));
    }

    public void start_20_ws_ota() {
        UnitoManager.getSingleton().isota = 0;
        starOTA();
    }

    public void start_ws_ota() {
        isBoth = false;
        getWSFile();
        UnitoManager.getSingleton().isota = 0;
        socketWriteData(selectType((byte) 0x01, deviceUuid));
    }

    public void start_hub_ota() {
        isBoth = false;
        UnitoManager.getSingleton().isota = 5;
        socketWriteData(selectType((byte) 0x00, deviceUuid));
    }

    public void start_ws_and_hub() {
        isBoth = true;
        getWSFile();
        UnitoManager.getSingleton().isota = 0;
        socketWriteData(selectType((byte) 0x01, deviceUuid));
    }
}
