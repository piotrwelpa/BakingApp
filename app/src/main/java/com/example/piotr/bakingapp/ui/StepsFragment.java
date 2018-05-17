package com.example.piotr.bakingapp.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    private static Bundle bundleLinearLayoutState;
    private ArrayList<Step> stepList;
    private int stepNumber = 0;

    private SimpleExoPlayer exoPlayer;

    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;
    @BindView(R.id.short_desc_steps_tv)
    TextView shortDescSteps;

    @BindView(R.id.desc_steps_tv)
    TextView descSteps;


    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_steps,
                container, false);
        ButterKnife.bind(this, rootView);
        playerView.setDefaultArtwork(BitmapFactory.decodeResource(
                getResources(), R.drawable.no_cake));
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

    public void decreaseStepNumber() {
        if (stepNumber > 0) {
            stepNumber--;
            populateUi();
        } else {
            Toast.makeText(getContext(), "First step reached.", Toast.LENGTH_SHORT).show();
        }
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    private void populateUi() {
        releasePlayer();
        shortDescSteps.setText(String.valueOf(stepList.get(stepNumber).getShortDescription()));
        descSteps.setText(String.valueOf(stepList.get(stepNumber).getDescription()));
        Uri uri = Uri.parse(stepList.get(stepNumber).getVideoURL());
        if (uri != null)
            initializePlayer(uri);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleLinearLayoutState = new Bundle();
        bundleLinearLayoutState.putParcelableArrayList(UiHelper.KEY_INGREDIENT_LIST, stepList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}