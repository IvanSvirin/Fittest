package com.example.ivansv.fittest.background;

import com.example.ivansv.fittest.model.DataList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestInterface {
    @GET("videos")
    Call<DataList> getData();
}
