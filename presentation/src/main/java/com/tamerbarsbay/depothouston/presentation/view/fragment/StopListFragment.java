package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.StopComponent;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.StopListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.StopListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.StopListAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StopListFragment extends BaseFragment implements StopListView {

    public interface StopListListener {
        void onStopClicked(final StopModel stopModel);
    }

    @Inject
    StopListPresenter stopListPresenter;

    @Bind(R.id.rv_stop_list)
    RecyclerView rvStops;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    private StopListAdapter stopsAdapter;
    private LinearLayoutManager stopsLayoutManager;

    private StopListListener stopListListener;

    private String routeId; //TODO we don't use this

    private static final String ARGUMENT_KEY_ROUTE_ID = "com.tamerbarsbay.depothouston.ARGUMENT_ROUTE_ID";

    public StopListFragment() {}

    /**
     * Create a new instance of a StopListFragment using the id of the route for which
     * to pull stops.
     * @param routeId The id of the route for which we are loading stops.
     * @return New StopListFragment object.
     */
    public static StopListFragment newInstance(String routeId) {
        StopListFragment fragment = new StopListFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_KEY_ROUTE_ID, routeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StopListListener) {
            this.stopListListener = (StopListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_stop_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    private void setupUI() {
        stopsLayoutManager = new LinearLayoutManager(getContext());
        rvStops.setLayoutManager(stopsLayoutManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadStopList();
    }

    @Override
    public void onResume() {
        super.onResume();
        stopListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopListPresenter.destroy();
    }

    private void initialize() {
        getComponent(StopComponent.class).inject(this);
        stopListPresenter.setView(this);
    }

    @Override
    public void renderStopList(Collection<StopModel> stopModels) {
        if (stopModels != null) {
            if (stopsAdapter == null) {
                stopsAdapter = new StopListAdapter(getActivity(), stopModels);
            } else {
                stopsAdapter.setStopsCollection(stopModels);
            }
            stopsAdapter.setOnItemClickListener(onItemClickListener);
            rvStops.setAdapter(stopsAdapter);
        }
    }

    @Override
    public void viewStop(StopModel stopModel) {
        if (stopListListener != null) {
            stopListListener.onStopClicked(stopModel);
        }
    }

    @Override
    public void showLoading() {
        rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
        rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        rlRetry.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        // TODO
    }

    @Override
    public void hideEmpty() {
        // TODO
    }

    @Override public void showError(String message) {
        showToastMessage(message);
    }

    @Override public Context getContext() {
        return getActivity().getApplicationContext();
    }

    private void loadStopList() {
        stopListPresenter.initialize();
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        StopListFragment.this.loadStopList();
    }

    private StopListAdapter.OnItemClickListener onItemClickListener =
            new StopListAdapter.OnItemClickListener() {
                @Override
                public void onStopItemClicked(StopModel stopModel) {
                    if (StopListFragment.this.stopListPresenter != null && stopModel != null) {
                        StopListFragment.this.stopListPresenter.onStopClicked(stopModel);
                    }
                }
            };
}
