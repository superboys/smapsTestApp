package com.unito.smapstestapp;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.unito.smapssdk.UnitoManager;
import com.unito.smapssdk.library.NotifyResponse;
import com.unito.smapssdk.library.ThreadPoolUtil;
import com.unito.smapstestapp.adapter.DataTypeAdapter;
import com.unito.smapstestapp.adapter.JsonRequestAdapter;
import com.unito.smapstestapp.databinding.ActivityMainBinding;
import com.unito.smapstestapp.view.MyDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements NotifyResponse {

    private ActivityMainBinding binding;
    private LinearLayoutManager mLayoutManager;
    private JsonRequestAdapter jsonRequestAdapter;
    private List<Map<String, Object>> jsonRequestList;
    private Map sendJsonMap;
    private UnitoManager unitoManager;
//    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        actionBar = this.getSupportActionBar();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        unitoManager = new UnitoManager(this);
        unitoManager.registerUnitoWaterSystemNotify(this);
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
//            if (unitoManager.getConnectStatus()) {
//                actionBar.setSubtitle("BLE connected");
//            } else {
//                actionBar.setSubtitle("BLE disConnected");
//            }
//        }

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if (unitoManager.target.equals("bleConnectToWaterSystem")) {
                    if (null != unitoManager.mac) {
                        unitoManager.bleConnectToWaterSystem();
                    }
                    return;
                } else if (unitoManager.target.equals("bleDisconnectFromWaterSystem")) {
                    unitoManager.bleDisconnectFromWaterSystem();
                    return;
                }
//                else if (unitoManager.msgId == 141) {
//                    Map map = new LinkedHashMap();
//                    map.put("msgId", unitoManager.msgId);
//                    map.put("destination", "appBle");
//                    map.put("source", "waterSystem");
//                    map.put("msgType", "response");
//                    map.put("target", "changeLanguage");
//                    map.put("response", "ack");
//                    LiveDataBus.get().with("showComConvertJson").setValue(new Gson().toJson(map));
//                    binding.tvFunction.setText("response");
//                    return;
//                } else if (unitoManager.msgId == 145) {
//                    Map map = new LinkedHashMap();
//                    map.put("msgId", unitoManager.msgId);
//                    map.put("destination", "appBle");
//                    map.put("source", "waterSystem");
//                    map.put("msgType", "response");
//                    map.put("target", "startOTA");
//                    map.put("response", "ack");
//                    LiveDataBus.get().with("showComConvertJson").setValue(new Gson().toJson(map));
//                    binding.tvFunction.setText("response");
//                    return;
//                }
                if (null != unitoManager.sendBytes) {
                    ThreadPoolUtil.handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.Q)
                        @Override
                        public void run() {
                            unitoManager.writeDirData(unitoManager.sendBytes);
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
//                        unitoManager.target = (String) dataMap.get("target");
                        binding.tvJsonData.bindJson(new Gson().toJson(unitoManager.sendCommand(dataMap)));
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

    /**
     * 请求权限
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_CODE)
    private void requestPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,};
        if (EasyPermissions.hasPermissions(this, perms)) {
            unitoManager.search(false);
        } else {
            // 没有权限
            EasyPermissions.requestPermissions(this, "App needs location permission", REQUEST_PERMISSION_CODE, perms);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    public void unitoWaterSystemNotify(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            binding.tvFunction.setText(jsonObject.optString("msgType"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        binding.tvJsonData.bindJson(s);
        binding.tvJsonData.expandAll();
    }
}