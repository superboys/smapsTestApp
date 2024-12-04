package com.unito.smapstestapp;

import static com.unito.smapssdk.library.BLEConstant.setRequestForDirModeFlashProcess;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.heaton.blelibrary.ble.BleLog;
import com.heaton.blelibrary.ble.callback.BleScanCallback;
import com.unito.smapssdk.UnitoManager;
import com.unito.smapssdk.chart.BarChartFrag;
import com.unito.smapssdk.chart.BoilerLineChartFrag;
import com.unito.smapssdk.chart.ChillerLineChartFrag;
import com.unito.smapssdk.chart.PieChartFrag;
import com.unito.smapssdk.chart.TestActivity;
import com.unito.smapssdk.library.BleRssiDevice;
import com.unito.smapssdk.library.NotifyResponse;
import com.unito.smapssdk.library.ThreadPoolUtil;
import com.unito.smapssdk.library.Utils;
import com.unito.smapstestapp.adapter.DataTypeAdapter;
import com.unito.smapstestapp.adapter.JsonRequestAdapter;
import com.unito.smapstestapp.databinding.ActivityMainBinding;
import com.unito.smapstestapp.view.MyDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NotifyResponse, UnitoManager.ISearchBleCallback, UnitoManager.IGetHubtokenCallback {

    private ActivityMainBinding binding;
    private LinearLayoutManager mLayoutManager;
    private JsonRequestAdapter jsonRequestAdapter;
    private List<Map<String, Object>> jsonRequestList;
    private Map sendJsonMap;
    //    private ActionBar actionBar;
    private List<String> commandList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBar = this.getSupportActionBar();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

//        UnitoManager.getSingleton();
        UnitoManager.getSingleton().registerUnitoWaterSystemNotify(this, this);
        ThreadPoolUtil.handler.post(new Runnable() {
            @Override
            public void run() {
                jsonRequestList = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>() {
                }.getType(), new DataTypeAdapter()).create().fromJson(UnitoApplication.getInstance().jsonRequests, new TypeToken<List<Map<String, Object>>>() {
                }.getType());

                initData();
                initView();
            }
        });

//        if (null != actionBar) {
//            if (UnitoManager.getConnectStatus()) {
//                actionBar.setSubtitle("BLE connected");
//            } else {
//                actionBar.setSubtitle("BLE disConnected");
//            }
//        }

//        UnitoManager.getSingleton(this).getBleDevices(this);

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (UnitoManager.getSingleton().target.equals("scanBle")) {
                    UnitoManager.getSingleton().getBleDevices(MainActivity.this);
                    return;
                } else if (UnitoManager.getSingleton().target.equals("bleConnectToWaterSystem")) {
//                    if (null != UnitoManager.getSingleton().mac) {
                    UnitoManager.getSingleton().bleConnectToWaterSystem();
//                    }
                    return;
                } else if (UnitoManager.getSingleton().target.equals("pie")) {
//                    if (null != UnitoManager.getSingleton().mac) {
                    startActivity(new Intent(MainActivity.this,PieChartFrag.class));

//                    }
                    return;
                }else if (UnitoManager.getSingleton().target.equals("bar")) {
//                    if (null != UnitoManager.getSingleton().mac) {
                    startActivity(new Intent(MainActivity.this, BarChartFrag.class));

//                    }
                    return;
                }else if (UnitoManager.getSingleton().target.equals("boilingLine")) {
//                    if (null != UnitoManager.getSingleton().mac) {
                    startActivity(new Intent(MainActivity.this, BoilerLineChartFrag.class));

//                    }
                    return;
                } else if (UnitoManager.getSingleton().target.equals("chilledLine")) {
//                    if (null != UnitoManager.getSingleton().mac) {
                    startActivity(new Intent(MainActivity.this, ChillerLineChartFrag.class));

//                    }
                    return;
                } else if (UnitoManager.getSingleton().target.equals("bleDisconnectFromWaterSystem")) {
                    UnitoManager.getSingleton().bleDisconnectFromWaterSystem();
                    return;
                } else if (UnitoManager.getSingleton().target.equals("start_20_OTA")) {
//                    UnitoManager.getSingleton().start_20_ws_ota();
                    return;
                } else if (UnitoManager.getSingleton().target.equals("Turn on wifi")) {
                    UnitoManager.getSingleton().openHotPort();
                    return;
                } else if (UnitoManager.getSingleton().target.equals("connect_wifi")) {
                    UnitoManager.getSingleton().connectHotPort();
                    return;
                } else if (UnitoManager.getSingleton().target.equals("ota_ws_and_hub")) {
                    UnitoManager.getSingleton().starESP();
                    return;
                } else if (UnitoManager.getSingleton().target.equals("downLoadOtaFile")) {
                    UnitoManager.getSingleton().downLoadOtaFile(MainActivity.this,Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID).getBytes());
                    return;
                }
                if (null != UnitoManager.getSingleton().sendBytes) {
                    ThreadPoolUtil.handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void run() {
                            UnitoManager.getSingleton().writeDirData(UnitoManager.getSingleton().sendBytes);
//                            UnitoManager.getSingleton().get40HubToken();
//                            UnitoManager.getSingleton().writeDirData(UnitoManager.getSingleton().sendBytes);
                        }
                    });
                    binding.tvFunction.setText("response");
                } else {
                    Toast.makeText(MainActivity.this, "Dir Off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        jsonRequestAdapter = new JsonRequestAdapter(jsonRequestList);
    }


    private void initView() {
        // 设置布局管理器
        binding.recyclerView.setLayoutManager(mLayoutManager);
        binding.recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // 设置adapter
        binding.recyclerView.setAdapter(jsonRequestAdapter);
        jsonRequestAdapter.setGetListener(new JsonRequestAdapter.GetListener() {

            @Override
            public void onClick(int position) {
                if (position == 2) {
                    UnitoManager.getSingleton().target = "getHubStatus";
                }
//                把点击的下标回传给适配器 确定下标
                jsonRequestAdapter.setmPosition(position);
                jsonRequestAdapter.notifyDataSetChanged();
                ThreadPoolUtil.handler.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        sendJsonMap = jsonRequestList.get(position);
                        Map dataMap = (Map) sendJsonMap.get("json");
                        binding.tvFunction.setText(dataMap.get("msgType") + "");
                        UnitoManager.target = (String) dataMap.get("target");
                        binding.tvJsonData.bindJson(new Gson().toJson(UnitoManager.getSingleton().sendCommand(dataMap)));
                        binding.tvJsonData.expandAll();
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 权限请求码
     */
    public static final int REQUEST_PERMISSION_CODE = 9527;
    public static final int RC_WRITE_AND_READ_EXTERNAL_STORAGE = 300;//识别码

    /**
     * 请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_CODE)
    private void requestPermission() {
        String[] perms;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
        } else {
            perms = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};
        }

        if (EasyPermissions.hasPermissions(this, perms)) {
//            UnitoManager.getSingleton().search(false);
        } else {
            // 没有权限
            EasyPermissions.requestPermissions(this, "App needs location permission", REQUEST_PERMISSION_CODE, perms);
        }
    }

//    @AfterPermissionGranted(RC_WRITE_AND_READ_EXTERNAL_STORAGE) //This is optional
//    private void requireWriteAndReadExternalStoragePermission() {
//        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            // 已经拥有相关权限
//        } else {
//            // 没有获取相关权限 去申请权限
//            EasyPermissions.requestPermissions(this, "App needs storage permission",
//                    RC_WRITE_AND_READ_EXTERNAL_STORAGE, perms);
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
//        requireWriteAndReadExternalStoragePermission();
    }

    @Override
    public void unitoWaterSystemNotify(String s) {
        Log.d("WatSystemNotify: ", s);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            binding.tvFunction.setText(jsonObject.optString("msgType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.tvJsonData.bindJson(s);
        binding.tvJsonData.expandAll();

    }

    @Override
    public void onBleDeviceFind(List<BleRssiDevice> devices) {
        if (null != devices && devices.size() > 0) {
            for (int i = 0; i < devices.size(); i++) {
                BleLog.e(devices.get(i).getBleName() + "   ", devices.get(i).getBleAddress() + "  " + devices.get(i).getRssi());
            }
        }
    }

    @Override
    public void onGetHubtoken(JSONObject jsonObject) {
        BleLog.e("token--->", jsonObject.toString());
    }
}