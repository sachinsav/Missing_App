package com.dream.te;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String baseurl = "http://10.0.2.2:8000/";
    private static Retrofit retrofit;

    public static Retrofit getAppClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(baseurl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
