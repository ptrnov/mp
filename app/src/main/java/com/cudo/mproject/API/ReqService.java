package com.cudo.mproject.API;

import com.cudo.mproject.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adsxg on 12/12/2017.
 */

public class ReqService {
    private static EPService EPService;
    public static EPService getAPIInterFace() {
        if (EPService == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BuildConfig.END_POINT)
                    .client(client)
                    .build();
            EPService = retrofit.create(EPService.class);
        }
        return EPService;
    }
}
