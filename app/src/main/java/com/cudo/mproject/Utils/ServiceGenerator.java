package com.cudo.mproject.Utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
    get source : http://www.asifmoinulislam.com/posting-raw-json-data-using-retrofit/
 */
public class ServiceGenerator {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {

        Retrofit retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofitBuilder.create(serviceClass);
    }
}
