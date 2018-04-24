package com.example.piotr.bakingapp.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    private ArrayList<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        setStepList(getIntent().getExtras().getParcelableArrayList(
                UiHelper.KEY_STEPS_LIST));
        populatePhoneUi();
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    private void populatePhoneUi(){
        StepsFragment stepsFragment = new StepsFragment();

        stepsFragment.setStepList(stepList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.steps_container, stepsFragment)
                .commit();
    }
}
