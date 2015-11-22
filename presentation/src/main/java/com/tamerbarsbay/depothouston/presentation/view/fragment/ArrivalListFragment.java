package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.app.Activity;
import android.app.Fragment;
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
import com.tamerbarsbay.depothouston.presentation.internal.di.components.ArrivalComponent;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.presenter.ArrivalListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.ArrivalListAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArrivalListFragment extends BaseFragment implements ArrivalListView {

    public interface ArrivalListListener {
        void onArrivalClicked(ArrivalModel arrivalModel);
    }

    @Inject
    ArrivalListPresenter arrivalListPresenter;

    @Bind(R.id.rv_arrival_list)
    RecyclerView rvArrivals;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    private ArrivalListAdapter arrivalsAdapter;
    private LinearLayoutManager arrivalsLayoutManager;

    private ArrivalListListener arrivalListListener;

    private static final String ARGUMENT_KEY_STOP_ID = "com.tamerbarsbay.depothouston.ARGUMENT_STOP_ID";

    public ArrivalListFragment() {}

    /**
     * Create a new instance of an ArrivalListFragment using the id of the stop for which
     * to pull arrivals.
     * @param stopId The id of the stop for which we are loading arrivals.
     * @return New ArrivalListFragment object.
     */
    public static ArrivalListFragment newInstance(String stopId) {
        ArrivalListFragment fragment = new ArrivalListFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_KEY_STOP_ID, stopId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ArrivalListListener) {
            this.arrivalListListener = (ArrivalListListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_arrival_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    private void setupUI() {
        this.arrivalsLayoutManager = new LinearLayoutManager(getContext());
        this.rvArrivals.setLayoutManager(arrivalsLayoutManager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.initialize();
        this.loadArrivalList();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.arrivalListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.arrivalListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.arrivalListPresenter.destroy();
    }

    private void initialize() {
        this.getComponent(ArrivalComponent.class).inject(this);
        this.arrivalListPresenter.setView(this);
    }

    @Override
    public void renderArrivalList(Collection<ArrivalModel> arrivalModels) {
        if (arrivalModels != null) {
            if (this.arrivalsAdapter == null) {
                this.arrivalsAdapter = new ArrivalListAdapter(getActivity(), arrivalModels);
            } else {
                this.arrivalsAdapter.setArrivalsCollection(arrivalModels);
            }
            this.arrivalsAdapter.setOnItemClickListener(onItemClickListener);
            this.rvArrivals.setAdapter(arrivalsAdapter);
        }
    }

    @Override
    public void showLoadingView() {
        this.rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        this.rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        this.rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {
        this.rlRetry.setVisibility(View.GONE);
    }

    @Override public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    private void loadArrivalList() {
        this.arrivalListPresenter.initialize();
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        ArrivalListFragment.this.loadArrivalList();
    }

    private ArrivalListAdapter.OnItemClickListener onItemClickListener =
            new ArrivalListAdapter.OnItemClickListener() {
                @Override
                public void onArrivalItemClicked(ArrivalModel arrivalModel) {
                    if (ArrivalListFragment.this.arrivalListPresenter != null && arrivalModel != null) {
                        //ArrivalListFragment.this.arrivalListPresenter.onArrivalClicked(arrivalModel);
                        //TODO necessary?
                    }
                }
            };

}
