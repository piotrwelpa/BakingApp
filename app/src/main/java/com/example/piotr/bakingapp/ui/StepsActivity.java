package com.example.piotr.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

public class StepsActivity extends AppCompatActivity {

    private ArrayList<Step> stepList;
    private StepsFragment stepsFragment;
    private PlayerFragment playerFragment;
    private long playerPosition = 0;

    private float x1;
    private float x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStepList(getIntent().getExtras().getParcelableArrayList(
                UiHelper.KEY_STEPS_LIST));

        if (savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong(UiHelper.EXO_PLAYER_STATE);
        }

        if (UiHelper.isVerticalOrientation(this)) {
            populatePhoneUi(savedInstanceState);
        } else {
            if (savedInstanceState != null)
                populateHorizontalUi(savedInstanceState);
        }
        setContentView(R.layout.activity_steps);

    }

    private void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    private void populatePhoneUi(Bundle savedInstanceState) {
        stepsFragment = new StepsFragment();

        stepsFragment.setStepList(stepList);
        stepsFragment.setPlayerPosition(playerPosition);

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
        playerFragment.setPlayerPosition(playerPosition);


        FragmentManager fragmentManager = getSupportFragmentManager();

        playerFragment.setStepNumber(savedInstanceState.getInt(UiHelper.STEP_NUMBER_KEY));
        fragmentManager.beginTransaction()
                .replace(R.id.steps_container, playerFragment)
                .commit();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (UiHelper.isVerticalOrientation(this)) {
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
        }
        return super.onTouchEvent(event);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (stepsFragment != null) {
            outState.putInt(UiHelper.STEP_NUMBER_KEY, stepsFragment.getStepNumber());
            outState.putLong(UiHelper.EXO_PLAYER_STATE, stepsFragment.getPlayerPosition());
            //stepsFragment.getPlayerPosition()
        } else if (playerFragment != null) {
            outState.putInt(UiHelper.STEP_NUMBER_KEY, playerFragment.getStepNumber());
            outState.putLong(UiHelper.EXO_PLAYER_STATE, playerFragment.getPlayerPosition());
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return false;
        }
    }
}
