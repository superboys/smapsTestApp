package com.unito.smapstestapp;

import android.app.Application;

import com.unito.smapssdk.library.ThreadPoolUtil;
import com.unito.smapssdk.library.Utils;

public class UnitoApplication extends Application {

    private static UnitoApplication sInstance;
//    public JsonRequestsModel arryJsonRequest = new JsonRequestsModel();
    public String jsonRequests = "";


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        ThreadPoolUtil.handler.post(new Runnable() {
            @Override
            public void run() {
                jsonRequests = Utils.loadJSONFromAsset(sInstance, "JsonRequests.json");
            }
        });

    }

    public static UnitoApplication getInstance() {
        return sInstance;
    }

}
