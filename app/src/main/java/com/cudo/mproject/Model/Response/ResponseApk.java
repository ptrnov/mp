package com.cudo.mproject.Model.Response;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

public class ResponseApk {
    String TAG = getClass().getSimpleName();
    @Getter
    @Setter
    boolean status;
    @Getter
    @Setter
    String text;
    @Getter
    @Setter
    String versiApk;
    @Getter
    @Setter
    String link;

    public boolean status() {
//        Boolean isiStatus = null;
//        if(status !=false)
       return status;
    }
    public String text() {
        String isiText = null;
        if (!text.isEmpty()) {
            isiText =text;
            Log.d(TAG, "onResponse doApk isiResponse text: " + text);
        }
        return isiText;
    }
    public String link() {
        String isiResponse = null;
        if (!link.isEmpty()) {
            isiResponse =link;
            Log.d(TAG, "onResponse doApk isiResponse link: " + link);
        }
        return isiResponse;
    }
}
