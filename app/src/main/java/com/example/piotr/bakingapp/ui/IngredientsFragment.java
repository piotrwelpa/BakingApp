package com.example.piotr.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.ui.adapter.IngredientListAdapter;
import com.example.piotr.bakingapp.utils.UiHelper;

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

    @BindView(R.id.cake_ingredients_list)
    ListView ingredientsListView;
    @BindView(R.id.show_steps_btn)
    Button showSetps;

    public IngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_ingredients,
                container, false);
        ButterKnife.bind(this, rootView);

        if (ingredientList == null){
            setIngredients(bundleLinearLayoutState
                    .getParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST));
        }

        if (!UiHelper.isPhone(getContext())){
            showSetps.setVisibility(View.GONE);
        }else{
            setButtonClickListener();
        }

        initIngredientListAdapter(rootView.getContext());

        return rootView;
    }

    private void initIngredientListAdapter(Context context) {

        ingredientListAdapter = new IngredientListAdapter(
                (ArrayList<Ingredient>) ingredientList,
                context);


        ingredientsListView.setAdapter(ingredientListAdapter);
    }

    private void clearListAdapter(){
        if (ingredientListAdapter != null)
            ingredientListAdapter.clear();
    }

    private void setButtonClickListener() {
        showSetps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStepButtonClick(v);
            }
        });
    }

    public void showStepButtonClick(View v){
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

    @Override
    public void onPause() {
        super.onPause();
        bundleLinearLayoutState = new Bundle();
        Parcelable listState = ingredientsListView.onSaveInstanceState();
        bundleLinearLayoutState.putParcelable(UiHelper.KEY_INGREDIENT_LIST_STATE, listState);
        bundleLinearLayoutState.putParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST,
                (ArrayList<? extends Parcelable>) ingredientList);
        clearListAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleLinearLayoutState != null){
            Parcelable listState = bundleLinearLayoutState
                    .getParcelable(UiHelper.KEY_INGREDIENT_LIST_STATE);
            ingredientsListView.onRestoreInstanceState(listState);
        }
        initIngredientListAdapter(rootView.getContext());
    }

}
