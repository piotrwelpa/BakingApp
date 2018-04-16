package com.example.piotr.bakingapp.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.piotr.bakingapp.R;
import com.example.piotr.bakingapp.model.Cake;
import com.example.piotr.bakingapp.ui.adapter.MasterListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment {

    private List<Cake> cakeList;

    @BindView(R.id.master_list_rv)
    RecyclerView recyclerView;

    public MasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);

        setLayoutManager(rootView);

        MasterListAdapter adapter = new MasterListAdapter(cakeList);
        recyclerView.setAdapter(adapter);


        return rootView;
    }


    private void setLayoutManager(View rootView) {
        if (isPhone()){
            setLinearLayoutManager(rootView);
        }else {
            setGridLayoutManager(rootView);
        }
    }

    private void setLinearLayoutManager(View rootView){
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setGridLayoutManager(View rootView){
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(rootView.getContext(), getColumnCount());
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    private boolean isPhone() {
        try {
            getResources().getBoolean(R.bool.isTablet);
            return false;
        }catch(Resources.NotFoundException e){
            return true;
        }

    }

    private int getColumnCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    public void setCakeList(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

}
