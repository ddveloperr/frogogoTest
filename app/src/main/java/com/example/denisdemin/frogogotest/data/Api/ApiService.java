package com.example.denisdemin.frogogotest.data.Api;

import com.example.denisdemin.frogogotest.data.Api.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @Headers("content-type: application/json")
    @GET("users.json")
    Observable<Response<List<User>>> getUserList();

    @Headers("content-type: application/json")
    @POST("users.json")
    Observable<Response<User>> createNewUser();

    @Headers("content-type: application/json")
    @PATCH("users/{id}.json")
    Observable<Response<User>> editUser(@Path("id") Integer id);
}
