package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.DividerItemDecoration;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SavedStopAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedStopsFragment extends BaseFragment {
    //TODO implement SavedStopView?

    public interface SavedStopsListener {
        void onStopClicked(StopModel stopModel);
    }

    @Bind(R.id.rv_saved_stops)
    RecyclerView rvSavedStops;

    private SavedStopAdapter savedStopAdapter;
    private SavedStopsListener savedStopsListener;

    public SavedStopsFragment() {}

    public static SavedStopsFragment newInstance() {
        return new SavedStopsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_saved_stops, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("SavedStopsFragment", "onViewCreated"); //TODO temp

        RecyclerViewExpandableItemManager itemManager = new RecyclerViewExpandableItemManager(savedInstanceState); //TODO what

        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();

        savedStopAdapter = new SavedStopAdapter(getContext(), itemManager);
        savedStopAdapter.setSavedStopsListener(new SavedStopAdapter.SavedStopsListener() {
            @Override
            public void onGroupItemRemoved(int groupPosition) {
                //TODO
                Log.d("SavedStopsFragment", "onGroupItemRemoved: " + groupPosition);
            }

            @Override
            public void onChildItemRemoved(int groupPosition, int childPosition) {
                //TODO
                Log.d("SavedStopsFragment", "onChildItemRemoved: " + groupPosition);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                //TODO
                Log.d("SavedStopsFragment", "onItemViewClicked");
            }
        });

        RecyclerView.Adapter wrappedAdapter = itemManager.createWrappedAdapter(savedStopAdapter);
        wrappedAdapter = dragDropManager.createWrappedAdapter(wrappedAdapter);

        rvSavedStops.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSavedStops.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rvSavedStops.setAdapter(wrappedAdapter);

        dragDropManager.attachRecyclerView(rvSavedStops);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SavedStopsListener) {
            savedStopsListener = (SavedStopsListener) context;
        }
    }
}
