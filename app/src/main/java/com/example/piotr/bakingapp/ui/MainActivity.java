package com.example.piotr.bakingapp.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;
import com.example.piotr.bakingapp.utils.CakeAPI;
import com.example.piotr.bakingapp.utils.CakeApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private static final String KEY_CAKE_LIST_STATE = "cake_list_state";
    private List<Cake> cakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isOnline()) {
            CakeAPI cakeApi = getCakeApiClient();
            Call<List<Cake>> call = getCallEndpoint(cakeApi);
            executeCall(call);
        } else {
            Toast.makeText(this, R.string.online_error_message, Toast.LENGTH_LONG)
                    .show();
        }
    }


    private CakeAPI getCakeApiClient() {
        CakeAPI cakeApi = CakeApiClient.getClient().create(CakeAPI.class);
        return cakeApi;
    }

    private Call<List<Cake>> getCallEndpoint(CakeAPI cakeApi) {
        Call<List<Cake>> call = cakeApi.getCakes(getString(R.string.cake_list_endpoint));
        Log.d(TAG, "Calling endpoint: " + getString(R.string.cake_list_endpoint));
        return call;
    }

    private void executeCall(Call<List<Cake>> call) {
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (response.isSuccessful()) {
                    cakeList = response.body();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cakeList.forEach(cake -> Log.d(TAG, cake.getName()));
                    }
                    populateUi(cakeList);

                } else {
                    Log.e(TAG, "Error in downloading json");
                    Log.e(TAG, String.valueOf(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void populateUi(List<Cake> cakeList) {
        MasterListFragment cakeListFragment = new MasterListFragment();
        cakeListFragment.setCakeList(cakeList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.item_container, cakeListFragment)
                .commit();
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_CAKE_LIST_STATE,
                (ArrayList<? extends Parcelable>) cakeList);
    }

}
