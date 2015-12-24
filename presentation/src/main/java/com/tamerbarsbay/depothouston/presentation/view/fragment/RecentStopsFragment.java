package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RecentStopModel;
import com.tamerbarsbay.depothouston.presentation.util.RecentStopUtils;
import com.tamerbarsbay.depothouston.presentation.view.adapter.RecentStopAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentStopsFragment extends BaseFragment {

    public interface RecentStopsListener {
        void onStopClicked(RecentStopModel stop);
    }

    @Bind(R.id.rv_recent_stop_list)
    RecyclerView rvRecentStops;

    @Bind(R.id.layout_recent_stops_empty)
    LinearLayout layoutEmpty;

    private RecentStopAdapter recentStopAdapter;

    private RecentStopsListener recentStopsListener;

    public RecentStopsFragment() {}

    public static RecentStopsFragment newInstance() {
        return new RecentStopsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecentStopsListener) {
            recentStopsListener = (RecentStopsListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_recent_stops, container, false);
        ButterKnife.bind(this, fragmentView);

        rvRecentStops.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<RecentStopModel> recentStops = RecentStopUtils.getRecentStops(getContext());
        if (recentStops.isEmpty()) {
            showEmptyView();
        } else {
            hideEmptyView();
            RecentStopAdapter recentStopAdapter = new RecentStopAdapter(getContext(), recentStops);
            recentStopAdapter.setOnItemClickListener(onItemClickListener);
            rvRecentStops.setAdapter(recentStopAdapter);
        }

        return fragmentView;
    }

    private void showEmptyView() {
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void hideEmptyView() {
        if (layoutEmpty != null) {
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private RecentStopAdapter.OnItemClickListener onItemClickListener =
            new RecentStopAdapter.OnItemClickListener() {
                @Override
                public void onStopClicked(RecentStopModel stop) {
                    if (stop != null && recentStopsListener != null) {
                        recentStopsListener.onStopClicked(stop);
                    }
                }
            };
}
