package com.example.piotr.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.ui.adapter.IngredientListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsFragment extends Fragment {

    public static final String KEY_INGREDIENTS = "key_ingredients";

    private IngredientListAdapter ingredientListAdapter;
    private List<Ingredient> ingredientList;
    private static Bundle bundleRLinearLayoutState;

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
        final View rootView = inflater.inflate(R.layout.fragment_ingredients,
                container, false);
        ButterKnife.bind(this, rootView);


        if (ingredientList == null){
            setIngredients(bundleRLinearLayoutState.getParcelableArrayList(KEY_INGREDIENTS));
        }

        initIngredientListAdapter(rootView.getContext());

        setButtonClickListener();


        return rootView;
    }

    private void initIngredientListAdapter(Context context) {

        ingredientListAdapter = new IngredientListAdapter(
                (ArrayList<Ingredient>) ingredientList,
                context);


        ingredientsListView.setAdapter(ingredientListAdapter);
    }

    private void setButtonClickListener() {
        showSetps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStepButtonClick();
            }
        });
    }

    public void showStepButtonClick(){
        Toast.makeText(getContext(), "CLICKED", Toast.LENGTH_SHORT).show();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredientList = ingredients;
    }


    @Override
    public void onPause() {
        super.onPause();
        bundleRLinearLayoutState = new Bundle();
        bundleRLinearLayoutState.putParcelableArrayList(KEY_INGREDIENTS,
                (ArrayList<? extends Parcelable>) ingredientList);
    }

}
