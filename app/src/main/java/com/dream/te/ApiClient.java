package com.dream.te;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String baseurl = "http://10.0.2.2:8000/";
    private static Retrofit retrofit;

    public static Retrofit getAppClient(){
        if(retrofit==null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10000, TimeUnit.SECONDS)
                    .readTimeout(10000,TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder().baseUrl(baseurl).client(client).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
