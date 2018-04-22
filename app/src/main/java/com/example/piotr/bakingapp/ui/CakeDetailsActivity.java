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
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;

import java.util.ArrayList;

public class CakeDetailsActivity extends AppCompatActivity {

    Cake cake;
    ArrayList<Ingredient> ingredientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        setCake(getIntent().getExtras().getParcelable(MasterListAdapter.KEY_CAKE_ITEM));
        setIngredientList(getIntent().getExtras()
                .getParcelableArrayList(MasterListAdapter.KEY_INGREDIENT_LIST));

        setTitle(cake.getName());

        if (isPhone()){
            populatePhoneUi();
        }else{
            populateTableUi();
        }
    }

    private boolean isPhone() {
        try {
            getResources().getBoolean(R.bool.isTablet);
            return false;
        }catch(Resources.NotFoundException e){
            return true;
        }
    }

    private void setCake(Cake cake) {
        this.cake = cake;
    }
    private void setIngredientList(ArrayList<Ingredient> ingredientList){
        this.ingredientList = ingredientList;
    }

    private void populatePhoneUi(){
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        ingredientsFragment.setIngredients(ingredientList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();
    }

    private void populateTableUi(){
        //TODO: populateTabletUi
    }
}
