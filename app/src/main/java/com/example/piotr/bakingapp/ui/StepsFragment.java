package com.example.piotr.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    private static Bundle bundleRLinearLayoutState;
    private ArrayList<Step> stepList;

    @BindView(R.id.text_view_steps)
    TextView textView;

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_steps,
                container, false);
        ButterKnife.bind(this, rootView);

        if (stepList == null){
            setStepList(bundleRLinearLayoutState.getParcelableArrayList(
                    UiHelper.KEY_INGREDIENT_LIST));
        }
        textView.setText(String.valueOf(stepList));
        return rootView;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRLinearLayoutState = new Bundle();
        bundleRLinearLayoutState.putParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST, stepList);
    }
}
