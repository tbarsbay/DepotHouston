package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.app.Activity;
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
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.presenter.RouteListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.RouteListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.RouteListAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Tamer on 7/23/2015.
 */
public class RouteListFragment extends BaseFragment implements RouteListView {

    public interface RouteListListener {
        void onRouteClicked(final RouteModel routeModel);
    }

    @Inject
    RouteListPresenter routeListPresenter;

    @InjectView(R.id.rv_route_list)
    RecyclerView rvRoutes;

    @InjectView(R.id.rl_progress)
    RelativeLayout rlProgress;

    @InjectView(R.id.rl_retry)
    RelativeLayout rlRetry;

    @InjectView(R.id.btn_retry)
    Button btnRetry;

    private RouteListAdapter routesAdapter;
    private LinearLayoutManager routesLayoutManager;

    private RouteListListener routeListListener;

    public RouteListFragment() {}

    public static RouteListFragment newInstance() {
        return new RouteListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof RouteListListener) {
            this.routeListListener = (RouteListListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_route_list, container, false);
        ButterKnife.inject(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadRouteList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.routeListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.routeListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.routeListPresenter.destroy();
    }

    private void initialize() {
        this.getComponent(RouteComponent.class).inject(this);
        this.routeListPresenter.setView(this);
    }

    private void setupUI() {
        this.routesLayoutManager = new LinearLayoutManager(getActivity());
        this.rvRoutes.setLayoutManager(routesLayoutManager);
    }

    @Override
    public void showLoading() {
        this.rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        this.rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
        this.rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rlRetry.setVisibility(View.GONE);
    }

    @Override
    public void renderRouteList(Collection<RouteModel> routeModels) {
        if (routeModels != null) {
            if (this.routesAdapter == null) {
                this.routesAdapter = new RouteListAdapter(getActivity(), routeModels);
            } else {
                this.routesAdapter.setRoutesCollection(routeModels);
            }
            this.routesAdapter.setOnItemClickListener(onItemClickListener);
            this.rvRoutes.setAdapter(routesAdapter);
        }
    }

    @Override
    public void viewRoute(RouteModel routeModel) {
        if (this.routeListListener != null) {
            this.routeListListener.onRouteClicked(routeModel);
        }
    }

    @Override public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    private void loadRouteList() {
        this.routeListPresenter.initialize();
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        RouteListFragment.this.loadRouteList();
    }

    private RouteListAdapter.OnItemClickListener onItemClickListener =
            new RouteListAdapter.OnItemClickListener() {
                @Override
                public void onRouteItemClicked(RouteModel routeModel) {
                    if (RouteListFragment.this.routeListPresenter != null && routeModel != null) {
                        RouteListFragment.this.routeListPresenter.onRouteClicked(routeModel);
                    }
                }
            };
}
