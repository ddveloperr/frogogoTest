package com.example.denisdemin.frogogotest.data.Api;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInit {
    private static String urlSite = "https://bb-test-server.herokuapp.com/";
    public static Retrofit getRetroObject(){
        return new Retrofit.Builder()
                .baseUrl(urlSite)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}