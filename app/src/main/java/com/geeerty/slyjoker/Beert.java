package com.geeerty.slyjoker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.UUID;

public class Beert extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        initAps();

    }

    private void initAps() {
        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                JSONObject json = new JSONObject(conversionData);
                write_key1(json.toString());
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                JSONObject json = new JSONObject();
                try {
                    json.put("error", errorMessage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                write_key1(json.toString());
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> attributionData) {
                for (String attrName : attributionData.keySet()) {
                    Log.d("LOG_TAG", "attribute: " + attrName + " = " + attributionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d("LOG_TAG", "error onAttributionFailure : " + errorMessage);
            }
        };

        AppsFlyerLib.getInstance().init("eoSxK3K3Af7itd55zr5WqA", conversionListener, this); //todo вставить ключ апса
        AppsFlyerLib.getInstance().start(this);
    }

    public void write_key1(String s) {
        SharedPreferences myPrefs = getSharedPreferences("file", Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("key1", s);
        editor.apply();
    }
}



