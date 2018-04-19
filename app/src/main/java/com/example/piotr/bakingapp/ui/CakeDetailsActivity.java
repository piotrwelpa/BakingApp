package com.example.piotr.bakingapp.ui;

import android.content.res.Resources;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;

public class CakeDetailsActivity extends AppCompatActivity {

    Cake cake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cake_details);

        setCake(getIntent().getExtras().getParcelable(MasterListAdapter.KEY_CAKE_ITEM));
        setTitle(cake.getName());

        if (isPhone()){
            populatePhoneUi();
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

    private void populatePhoneUi(){
        IngredientsFragment ingredientsFragment = new IngredientsFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.ingredients_container, ingredientsFragment)
                .commit();
    }
}
