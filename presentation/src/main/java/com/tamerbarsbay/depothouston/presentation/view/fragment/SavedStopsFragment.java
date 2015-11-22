package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamerbarsbay.depothouston.R;

import butterknife.ButterKnife;

public class SavedStopsFragment extends BaseFragment {


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

}
