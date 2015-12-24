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
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.presenter.RouteListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.RouteListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.RouteAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RouteListFragment extends BaseFragment implements RouteListView {

    public interface RouteListListener {
        void onRouteClicked(final RouteModel routeModel);
    }

    @Inject
    RouteListPresenter routeListPresenter;

    @Bind(R.id.rv_route_list)
    RecyclerView rvRoutes;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    private RouteAdapter routesAdapter;
    private LinearLayoutManager routesLayoutManager;

    private RouteListListener routeListListener;

    public RouteListFragment() {}

    public static RouteListFragment newInstance() {
        return new RouteListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RouteListListener) {
            routeListListener = (RouteListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_route_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadRouteList();
    }

    @Override
    public void onResume() {
        super.onResume();
        routeListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        routeListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        routeListPresenter.destroy();
    }

    private void initialize() {
        this.getComponent(RouteComponent.class).inject(this);
        routeListPresenter.setView(this);
    }

    private void setupUI() {
        routesLayoutManager = new LinearLayoutManager(getActivity());
        rvRoutes.setLayoutManager(routesLayoutManager);
    }

    @Override
    public void showLoadingView() {
        rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {
        rlRetry.setVisibility(View.GONE);
    }

    @Override
    public void renderRouteList(Collection<RouteModel> routeModels) {
        if (routeModels != null) {
            if (routesAdapter == null) {
                routesAdapter = new RouteAdapter(getActivity(), routeModels);
            } else {
                routesAdapter.setRoutesCollection(routeModels);
            }
            routesAdapter.setOnItemClickListener(onItemClickListener);
            rvRoutes.setAdapter(routesAdapter);
        }
    }

    @Override
    public void viewRoute(RouteModel routeModel) {
        if (routeListListener != null) {
            routeListListener.onRouteClicked(routeModel);
        }
    }

    @Override public void showError(String message) {
        showToastMessage(message);
    }

    @Override public Context getContext() {
        return getActivity().getApplicationContext();
    }

    private void loadRouteList() {
        routeListPresenter.initialize();
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        RouteListFragment.this.loadRouteList();
    }

    private RouteAdapter.OnItemClickListener onItemClickListener =
            new RouteAdapter.OnItemClickListener() {
                @Override
                public void onRouteItemClicked(RouteModel routeModel) {
                    if (RouteListFragment.this.routeListPresenter != null && routeModel != null) {
                        RouteListFragment.this.routeListPresenter.onRouteClicked(routeModel);
                    }
                }
            };
}
