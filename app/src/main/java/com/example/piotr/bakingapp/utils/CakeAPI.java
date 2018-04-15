package com.example.piotr.bakingapp.utils;

import com.example.piotr.bakingapp.model.Cake;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CakeAPI {
    @GET("/topher/2017/May/59121517_baking/{name}")
    Call<List<Cake>> getCakes(@Path("name") String name);
}
