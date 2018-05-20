package com.example.piotr.bakingapp.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Step;
import com.example.piotr.bakingapp.utils.UiHelper;
import com.google.android.exoplayer2.C;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepsFragment extends Fragment {

    private static Bundle bundleLinearLayoutState;
    private ArrayList<Step> stepList;
    private int stepNumber = 0;
    private long playerPosition;
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

    public long getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(long playerPosition) {
        this.playerPosition = playerPosition;
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
        Log.d("STEPS", "po RELEASE");
        shortDescSteps.setText(String.valueOf(stepList.get(stepNumber).getShortDescription()));
        descSteps.setText(String.valueOf(stepList.get(stepNumber).getDescription()));
        if (stepList.get(stepNumber).getThumbnailURL() != null
                && !stepList.get(stepNumber).getThumbnailURL().equals("")) {
            loadThumbnailToPlayer();
        }
        Uri uri = Uri.parse(stepList.get(stepNumber).getVideoURL());
        if (uri != null && !uri.toString().equals(""))
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

            exoPlayer.seekTo(playerPosition);

        }
    }

    private void loadThumbnailToPlayer() {
        Target image = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(() -> {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + "tmpFile");
                    try {
                        file.createNewFile();
                        FileOutputStream outstream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outstream);
                        outstream.close();
                        playerView.setDefaultArtwork(bitmap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get()
                .load(stepList.get(stepNumber).getThumbnailURL())
                .into(image);
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.prepare(null);
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
        playerPosition = exoPlayer.getCurrentPosition();
        releasePlayer();
    }

}