package com.example.piotr.bakingapp.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.example.piotr.bakingapp.utils.UiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MasterListFragment extends Fragment {

    private List<Cake> cakeList;
    private static Bundle bundleRecyclerViewState;


    @BindView(R.id.master_list_rv)
    RecyclerView recyclerView;

    public MasterListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);

        setLayoutManager(rootView);

        if (cakeList == null) {
            setCakeList(bundleRecyclerViewState.getParcelableArrayList(UiHelper.KEY_CAKE_LIST));
        }

        MasterListAdapter adapter = new MasterListAdapter(cakeList);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private void setLayoutManager(View rootView) {
        if (UiHelper.isPhone(getContext())) {
            setLinearLayoutManager(rootView);
        } else {
            setGridLayoutManager(rootView);
        }
    }

    private void setLinearLayoutManager(View rootView) {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void setGridLayoutManager(View rootView) {
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(rootView.getContext(),
                        UiHelper.getColumnCount(getActivity()));
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    public void setCakeList(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

    @Override
    public void onPause() {
        super.onPause();
        bundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundleRecyclerViewState.putParcelable(UiHelper.KEY_RECYCLER_STATE, listState);
        bundleRecyclerViewState.putParcelableArrayList(UiHelper.KEY_CAKE_LIST,
                (ArrayList<? extends Parcelable>) cakeList);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundleRecyclerViewState != null) {
            Parcelable listState = bundleRecyclerViewState
                    .getParcelable(UiHelper.KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }
}
