package com.example.piotr.bakingapp.ui;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

public class CakeDetailsActivity extends AppCompatActivity {

    Cake cake;
    ArrayList<Ingredient> ingredientList;
    ArrayList<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        setCake(getIntent().getExtras().getParcelable(UiHelper.KEY_CAKE_ITEM));
        setIngredientList(getIntent().getExtras()
                .getParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST));
        setStepList(getIntent().getExtras().getParcelableArrayList(
                UiHelper.KEY_STEPS_LIST));

        setTitle(cake.getName());

        if (UiHelper.isPhone(this)){
            populatePhoneUi();
        }else{
            populateTableUi();
        }
    }

    private void setCake(Cake cake) {
        this.cake = cake;
    }
    private void setIngredientList(ArrayList<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
    }
    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    private void populatePhoneUi(){
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        ingredientsFragment.setIngredients(ingredientList);
        ingredientsFragment.setStepList(stepList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();
    }

    private void populateTableUi(){
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setIngredients(ingredientList);

        StepsFragment stepsFragment = new StepsFragment();
        stepsFragment.setStepList(stepList);

        FragmentManager ingredientsFragmentManager = getSupportFragmentManager();
        ingredientsFragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();

        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        stepsFragmentManager.beginTransaction()
                .add(R.id.steps_container, stepsFragment)
                .commit();
    }
}
