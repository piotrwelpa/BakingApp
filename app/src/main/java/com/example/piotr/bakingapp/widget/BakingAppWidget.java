package com.example.piotr.bakingapp.widget;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.ui.IngredientsFragment;
import com.example.piotr.bakingapp.ui.MasterListFragment;
import com.example.piotr.bakingapp.ui.adapter.IngredientListAdapter;
import com.example.piotr.bakingapp.utils.CakeAPI;
import com.example.piotr.bakingapp.utils.CakeApiClient;
import com.example.piotr.bakingapp.utils.EspressoIdlingResouce;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    private static List<Cake> cakeList;
    private static final String TAG = BakingAppWidget.class.getSimpleName();
//    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        if (CakeApiClient.isOnline(context)) {
            CakeAPI cakeApi = CakeApiClient.getCakeApiClient();
            Call<List<Cake>> call = CakeApiClient.getCallEndpoint(cakeApi, context);
            executeCall(call, context, appWidgetManager, appWidgetId);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void executeCall(Call<List<Cake>> call, Context context,
                                    AppWidgetManager appWidgetManager, int appWidgetId) {
        EspressoIdlingResouce.increment();
        call.enqueue(new Callback<List<Cake>>() {
            @Override
            public void onResponse(Call<List<Cake>> call, Response<List<Cake>> response) {
                if (response.isSuccessful()) {
                    cakeList = response.body();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cakeList.forEach(cake -> Log.d(TAG, cake.getName()));
                    }
                    populateUi(cakeList, context, appWidgetManager, appWidgetId);

                } else {
                    Log.e(TAG, "Error in downloading json");
                    Log.e(TAG, String.valueOf(response.errorBody()));
                }
                EspressoIdlingResouce.decrement();
            }

            @Override
            public void onFailure(Call<List<Cake>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void populateUi(List<Cake> cakeList, Context context,
                                   AppWidgetManager appWidgetManager, int appWidgetId) {
        IngredientListAdapter ingredientListAdapter = new IngredientListAdapter(
                (ArrayList<Ingredient>) cakeList.get(0).getIngredients(),
                context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        StringBuilder list = new StringBuilder();
        for (Ingredient s: cakeList.get(0).getIngredients()) {
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

