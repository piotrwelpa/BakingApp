package com.example.piotr.bakingapp.ui;

import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.support.v7.app.ActionBar;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    private ArrayList<Step> stepList;
    StepsFragment stepsFragment;
    PlayerFragment playerFragment;

    public float x1, x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setStepList(getIntent().getExtras().getParcelableArrayList(
                UiHelper.KEY_STEPS_LIST));

        if (UiHelper.isVerticalOrientation(this)) {
            populatePhoneUi(savedInstanceState);
        } else {
            if (savedInstanceState != null)
                populateHorizontalUi(savedInstanceState);
        }
        setContentView(R.layout.activity_steps);

    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    private void populatePhoneUi(Bundle savedInstanceState) {
        stepsFragment = new StepsFragment();

        stepsFragment.setStepList(stepList);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.steps_container, stepsFragment)
                    .commit();
        } else {
            stepsFragment.setStepNumber(savedInstanceState.getInt(UiHelper.STEP_NUMBER_KEY));
            fragmentManager.beginTransaction()
                    .replace(R.id.steps_container, stepsFragment)
                    .commit();
        }
    }

    private void populateHorizontalUi(Bundle savedInstanceState) {
        final ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        playerFragment = new PlayerFragment();

        playerFragment.setStepList(stepList);

        FragmentManager fragmentManager = getSupportFragmentManager();

        playerFragment.setStepNumber(savedInstanceState.getInt(UiHelper.STEP_NUMBER_KEY));
        fragmentManager.beginTransaction()
                .replace(R.id.steps_container, playerFragment)
                .commit();

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
                        stepsFragment.decraseStepNumber();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (stepsFragment != null)
            outState.putInt(UiHelper.STEP_NUMBER_KEY, stepsFragment.getStepNumber());
        else if (playerFragment != null)
            outState.putInt(UiHelper.STEP_NUMBER_KEY, playerFragment.getStepNumber());
    }
}
