package com.example.piotr.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piotr.bakingapp.model.Cake;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CakeItemFragment extends Fragment {
    private static final String TAG = CakeItemFragment.class.getSimpleName();

    private static final String CAKE_ID_LIST = "cake_id";
    private static final String LIST_INDEX = "list_index";

    private List<Cake> cakeListIds;
    private int listIndex;

    ImageView cakeImage;
    TextView cakeName;

    public CakeItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            cakeListIds = savedInstanceState.getParcelableArrayList(CAKE_ID_LIST);
            listIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        View rootView = inflater.inflate(R.layout.fragment_cake_item, container, false);

        cakeName = rootView.findViewById(R.id.cake_name_item_cv);
        cakeImage = rootView.findViewById(R.id.cake_image_item_cv);

        if (cakeListIds != null){
            // TODO: Ustawienie image za pomocą picasso
            cakeImage.setImageResource(R.drawable.ic_launcher_background);
            Log.d(TAG, cakeListIds.get(listIndex).getName());
            cakeName.setText(cakeListIds.get(listIndex).getName());
            //TODO: OnClickListener
        }else{
            Log.v(TAG, "This fragment has a null list of cakes");
        }

        return rootView;
    }

    public void setCakeListIds(List<Cake> cakeListIds) {
        this.cakeListIds = cakeListIds;
    }

    public void setListIndex(int listIndex) {
        this.listIndex = listIndex;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(CAKE_ID_LIST, (ArrayList<Cake>)cakeListIds);
        outState.putInt(LIST_INDEX, listIndex);
    }
}
