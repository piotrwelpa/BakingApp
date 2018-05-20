package com.example.piotr.bakingapp.ui;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class PlayerFragment extends Fragment {

    private int stepNumber = 0;
    private static Bundle bundleLinearLayoutState;
    private long playerPosition;
    private ArrayList<Step> stepList;

    private SimpleExoPlayer exoPlayer;

    @BindView(R.id.playerView)
    SimpleExoPlayerView playerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_player,
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

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepList(ArrayList<Step> stepList) {
        this.stepList = stepList;
    }

    public long getPlayerPosition() {
        return exoPlayer.getCurrentPosition();
    }

    public void setPlayerPosition(long playerPosition) {
        this.playerPosition = playerPosition;
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

            exoPlayer.seekTo(playerPosition);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.prepare(null);
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    private void populateUi() {
        releasePlayer();
        Uri uri = Uri.parse(stepList.get(stepNumber).getVideoURL());
        if (uri != null)
            initializePlayer(uri);
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
