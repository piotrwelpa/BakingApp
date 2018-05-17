package com.example.piotr.bakingapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CakeApiClient {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net";
    private static Retrofit retrofit = null;
    private static final String TAG = CakeApiClient.class.getSimpleName();

    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static CakeAPI getCakeApiClient() {
        return CakeApiClient.getClient().create(CakeAPI.class);
    }

    public static Call<List<Cake>> getCallEndpoint(CakeAPI cakeApi, Context context) {
        Call<List<Cake>> call = cakeApi.getCakes(context.getString(R.string.cake_list_endpoint));
        Log.d(TAG, "Calling endpoint: " + context.getString(R.string.cake_list_endpoint));
        return call;
    }
}