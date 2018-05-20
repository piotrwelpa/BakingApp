package com.example.piotr.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.ui.adapter.IngredientListAdapter;
import com.example.piotr.bakingapp.utils.UiHelper;
import com.example.piotr.bakingapp.widget.BakingAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    private IngredientListAdapter ingredientListAdapter;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;
    private static Bundle bundleLinearLayoutState;
    private View rootView;
    private String cakeName;

    @BindView(R.id.cake_ingredients_list)
    RecyclerView ingredientsListView;
    @BindView(R.id.show_steps_btn)
    Button showSteps;

    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ingredients,
                container, false);
        ButterKnife.bind(this, rootView);

        setLayoutManager(rootView);

        if (ingredientList == null) {
            setIngredients(bundleLinearLayoutState
                    .getParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST));
        }

        if (!UiHelper.isPhone(getContext())) {
            showSteps.setVisibility(View.GONE);
        } else {
            setButtonClickListener();
        }

        initIngredientListAdapter(rootView.getContext());

        changeIngredientsOnWidgetToCurrent();
        return rootView;
    }

    private void setLayoutManager(View rootView) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(rootView.getContext());
        ingredientsListView.setLayoutManager(linearLayoutManager);
    }

    private void changeIngredientsOnWidgetToCurrent() {
        StringBuilder list = new StringBuilder();
        for (Ingredient s : ingredientList) {
            list.append(s.getQuantity()).append(" ")
                    .append(s.getMeasure()).append(" ")
                    .append(s.getIngredient())
                    .append("\n");
        }

        Context context = getContext();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidget.class);
        remoteViews.setTextViewText(R.id.ingredient_list_widget, list.toString());
        remoteViews.setTextViewText(R.id.cake_name_widget, cakeName);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void initIngredientListAdapter(Context context) {

        ingredientListAdapter = new IngredientListAdapter(ingredientList);


        ingredientsListView.setAdapter(ingredientListAdapter);
    }


    private void setButtonClickListener() {
        showSteps.setOnClickListener(this::showStepButtonClick);
    }

    private void showStepButtonClick(View v) {
        Intent stepActivity = new Intent(v.getContext(), StepsActivity.class);
        Bundle extrasBundle = new Bundle();

        extrasBundle.putParcelableArrayList(UiHelper.KEY_STEPS_LIST,
                (ArrayList<? extends Parcelable>) stepList);
        stepActivity.putExtras(extrasBundle);
        v.getContext().startActivity(stepActivity);

    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
    }

    public void setStepList(List<Step> stepList) {
        this.stepList = stepList;
    }

    public void setCakeName(String cakeName) {
        this.cakeName = cakeName;
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleLinearLayoutState = new Bundle();
        Parcelable listState = ingredientsListView.getLayoutManager().onSaveInstanceState();
        bundleLinearLayoutState.putParcelable(UiHelper.KEY_INGREDIENT_LIST_STATE, listState);
        bundleLinearLayoutState.putParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST,
                (ArrayList<? extends Parcelable>) ingredientList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleLinearLayoutState != null) {
            Parcelable listState = bundleLinearLayoutState
                    .getParcelable(UiHelper.KEY_INGREDIENT_LIST_STATE);
            ingredientsListView.getLayoutManager().onRestoreInstanceState(listState);
        }
        initIngredientListAdapter(rootView.getContext());
    }

}
