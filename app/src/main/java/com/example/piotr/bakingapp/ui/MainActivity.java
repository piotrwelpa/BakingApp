package com.example.piotr.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;
import com.example.piotr.bakingapp.utils.CakeAPI;
import com.example.piotr.bakingapp.utils.CakeApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CakeAPI cakeApi = getCakeApiClient();
        Call<List<Cake>> call = getCallEndpoint(cakeApi);
        executeCall(call);
    }

    private CakeAPI getCakeApiClient(){
        CakeAPI cakeApi = CakeApiClient.getClient().create(CakeAPI.class);
        return cakeApi;
    }

    private Call<List<Cake>> getCallEndpoint(CakeAPI cakeApi){
        Call<List<Cake>> call = cakeApi.getCakes(getString(R.string.cake_list_endpoint));
        Log.d(TAG, "Calling endpoint: " + getString(R.string.cake_list_endpoint));
        return call;
    }

    private void executeCall(Call<List<Cake>> call){
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (response.isSuccessful()) {
                    List<Cake> cakeList = response.body();
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

    private void populateUi(List<Cake> cakeList){
        MasterListFragment cakeListFragment = new MasterListFragment();
        cakeListFragment.setCakeList(cakeList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.item_container, cakeListFragment)
                .commit();
    }


}
