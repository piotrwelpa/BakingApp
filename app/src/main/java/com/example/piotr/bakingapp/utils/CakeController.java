package com.example.piotr.bakingapp.utils;

import android.os.Build;
import android.util.Log;

import com.example.piotr.bakingapp.model.Cake;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CakeController implements Callback<List<Cake>> {

    private static final String TAG = CakeController.class.getSimpleName();

    static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private List<Cake> cakeList = null;

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CakeAPI cakeAPI = retrofit.create(CakeAPI.class);

        Call<List<Cake>> call = cakeAPI.getCakes("baking.json");
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
        if (response.isSuccessful()){
            List<Cake> cakeList = response.body();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                cakeList.forEach(cake -> Log.d(TAG, cake.getName()));
            }
            this.cakeList = cakeList;
        } else {
            Log.e(TAG, "Error in downloading json");
            Log.e(TAG, String.valueOf(response.errorBody()));
        }
    }

    @Override
    public void onFailure(Call<List<Cake>> call, Throwable t) {
        t.printStackTrace();
    }

    public List<Cake> getCakeList() {
        return cakeList;
    }
}
