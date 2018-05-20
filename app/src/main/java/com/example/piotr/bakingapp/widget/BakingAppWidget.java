package com.example.piotr.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.utils.CakeAPI;
import com.example.piotr.bakingapp.utils.CakeApiClient;
import com.example.piotr.bakingapp.utils.EspressoIdlingResource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BakingAppWidget extends AppWidgetProvider {
    private static List<Cake> cakeList;
    private static final String TAG = BakingAppWidget.class.getSimpleName();

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId) {

        if (CakeApiClient.isOnline(context)) {
            CakeAPI cakeApi = CakeApiClient.getCakeApiClient();
            Call<List<Cake>> call = CakeApiClient.getCallEndpoint(cakeApi, context);
            executeCall(call, context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    private static void executeCall(Call<List<Cake>> call, Context context,
                                    AppWidgetManager appWidgetManager, int appWidgetId) {
        EspressoIdlingResource.increment();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cake>> call, @NonNull Response<List<Cake>> response) {
                if (response.isSuccessful()) {
                    cakeList = response.body();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cakeList.forEach(cake -> Log.d(TAG, cake.getName()));
                    }
                    initUiWithFirstCakeIngredients(cakeList, context, appWidgetManager, appWidgetId);

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

    private static void initUiWithFirstCakeIngredients(List<Cake> cakeList, Context context,
                                                       AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        StringBuilder list = new StringBuilder();
        for (Ingredient s : cakeList.get(0).getIngredients()) {
            list.append(s.getQuantity()).append(" ")
                    .append(s.getMeasure()).append(" ")
                    .append(s.getIngredient())
                    .append("\n");
        }
        views.setTextViewText(R.id.ingredient_list_widget, list.toString());
        views.setTextViewText(R.id.cake_name_widget, cakeList.get(0).getName());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}

