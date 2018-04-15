package com.example.piotr.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        MasterListAdapter adapter = new MasterListAdapter(cakeList);
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    public void setCakeList(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }
}
