package com.example.piotr.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    private static Bundle bundleLinearLayoutState;
    private ArrayList<Step> stepList;
    private int stepNumber = 0;

    @BindView(R.id.short_desc_steps_tv)
    TextView shortDescSteps;

    @BindView(R.id.desc_steps_tv)
    TextView descSteps;

    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;


    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_steps,
                container, false);
        ButterKnife.bind(this, rootView);

        if (stepList == null) {
            setStepList(bundleLinearLayoutState.getParcelableArrayList(
                    UiHelper.KEY_INGREDIENT_LIST));
        }

        populateUi();

        return rootView;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    public void incraseStepNumber() {
        if (stepNumber < stepList.size() - 1) {
            stepNumber++;
            populateUi();
        } else {
            Toast.makeText(getContext(), "Last step reached.", Toast.LENGTH_SHORT).show();
        }
    }

    public void decraseStepNumber() {
        if (stepNumber > 0) {
            stepNumber--;
            populateUi();
        } else {
            Toast.makeText(getContext(), "First step reached.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getStepNumber(){
        return stepNumber;
    }

    public void setStepNumber(int stepNumber){
        this.stepNumber = stepNumber;
    }

    private void populateUi() {
        shortDescSteps.setText(String.valueOf(stepList.get(stepNumber).getShortDescription()));
        descSteps.setText(String.valueOf(stepList.get(stepNumber).getDescription()));

    }

    @Override
    public void onPause() {
        super.onPause();
        bundleLinearLayoutState = new Bundle();
        bundleLinearLayoutState.putParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST, stepList);
    }

}