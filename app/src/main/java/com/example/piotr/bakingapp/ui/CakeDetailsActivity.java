package com.example.piotr.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.model.Ingredient;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

public class CakeDetailsActivity extends AppCompatActivity {

    private Cake cake;
    private ArrayList<Ingredient> ingredientList;
    private ArrayList<Step> stepList;
    private StepsFragment stepsFragment;

    private Bundle savedInstanceState;

    private float x1;
    private float x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        if (savedInstanceState != null) {
            this.savedInstanceState = savedInstanceState;
        }
    }

    private void initCake() {
        setCake(getIntent().getExtras().getParcelable(UiHelper.KEY_CAKE_ITEM));
    }

    private void initIngredientList() {
        setIngredientList(getIntent().getExtras()
                .getParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST));
    }

    private void initStepList() {
        setStepList(getIntent().getExtras().getParcelableArrayList(
                UiHelper.KEY_STEPS_LIST));
    }

    private void setCake(Cake cake) {
        this.cake = cake;
    }

    private void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    private void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    private void populatePhoneUi() {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        ingredientsFragment.setIngredients(ingredientList);
        ingredientsFragment.setStepList(stepList);
        ingredientsFragment.setCakeName(cake.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();
    }

    private void populateTableUi(Bundle savedInstanceState) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        ingredientsFragment.setIngredients(ingredientList);
        ingredientsFragment.setCakeName(cake.getName());

        stepsFragment = new StepsFragment();
        stepsFragment.setStepList(stepList);

        FragmentManager ingredientsFragmentManager = getSupportFragmentManager();
        ingredientsFragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();

        FragmentManager stepsFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            stepsFragmentManager.beginTransaction()
                    .add(R.id.steps_container, stepsFragment)
                    .commit();
        } else {
            stepsFragment.setStepNumber(savedInstanceState.getInt(UiHelper.STEP_NUMBER_KEY));
            stepsFragmentManager.beginTransaction()
                    .replace(R.id.steps_container, stepsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > UiHelper.MIN_DISTANCE) {
                    if (x2 > x1) {
                        // Previous step
                        stepsFragment.decreaseStepNumber();
                    } else {
                        //Next step
                        stepsFragment.incraseStepNumber();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCake();
        initIngredientList();
        initStepList();
        setTitle(cake.getName());
        if (UiHelper.isPhone(this)) {
            populatePhoneUi();
        } else {
            populateTableUi(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!UiHelper.isPhone(this))
            outState.putInt(UiHelper.STEP_NUMBER_KEY, stepsFragment.getStepNumber());
    }

}
