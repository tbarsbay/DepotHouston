package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecentStopsListener) {
            recentStopsListener = (RecentStopsListener) context;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_clear_recent_stops) {
            showClearStopsConfirmationDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
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
            recentStopAdapter = new RecentStopAdapter(getContext(), recentStops);
            recentStopAdapter.setOnItemClickListener(onItemClickListener);
            rvRecentStops.setAdapter(recentStopAdapter);
        }

        return fragmentView;
    }

    private void showClearStopsConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setMessage(R.string.clear_stops_are_you_sure)
                .setTitle(R.string.clear_recent_stops)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearRecentStops();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clearRecentStops() {
        RecentStopUtils.clearRecentStops(getContext());
        int childCount = rvRecentStops.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View v = rvRecentStops.getChildAt(i);
            animateStopExit(childCount, i, v);
        }
    }

    private void animateStopExit(int stopCount, int position, View stopView) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_fade_out);
        animation.setDuration(300);
        // If there are more than 10 items, only offset the animation for the first ten.
        if ((stopCount <= 10) || (position <= 10)) {
            animation.setStartOffset(50*position);
        } else {
            animation.setStartOffset(50*10);
        }
        animation.setFillAfter(true);

        if (position == 0) {
            // After the last (first) item animates, update the adapter
            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    showEmptyView();
                    recentStopAdapter.clearRecentStops();
                }
            });
        }
        stopView.startAnimation(animation);
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
