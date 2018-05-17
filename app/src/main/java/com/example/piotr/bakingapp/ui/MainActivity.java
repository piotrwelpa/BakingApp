package com.example.piotr.bakingapp.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.utils.CakeAPI;
import com.example.piotr.bakingapp.utils.CakeApiClient;
import com.example.piotr.bakingapp.utils.EspressoIdlingResource;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private List<Cake> cakeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CakeApiClient.isOnline(this)) {
            CakeAPI cakeApi = CakeApiClient.getCakeApiClient();
            Call<List<Cake>> call = CakeApiClient.getCallEndpoint(cakeApi, this);
            executeCall(call);
        } else {
            Toast.makeText(this, R.string.online_error_message, Toast.LENGTH_LONG)
                    .show();
        }
    }


    private void executeCall(Call<List<Cake>> call) {
        EspressoIdlingResource.increment();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cake>> call, @NonNull Response<List<Cake>> response) {
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
                EspressoIdlingResource.decrement();
            }

            @Override
            public void onFailure(@NonNull Call<List<Cake>> call, @NonNull Throwable t) {
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


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(UiHelper.KEY_CAKE_LIST,
                (ArrayList<? extends Parcelable>) cakeList);
        super.onSaveInstanceState(outState);
    }

}
