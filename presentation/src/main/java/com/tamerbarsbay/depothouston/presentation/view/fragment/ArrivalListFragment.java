package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.ArrivalComponent;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.model.RecentStopModel;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.ArrivalListPresenter;
import com.tamerbarsbay.depothouston.presentation.service.ActiveTrackingService;
import com.tamerbarsbay.depothouston.presentation.util.RecentStopUtils;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;
import com.tamerbarsbay.depothouston.presentation.view.ActiveTrackingMenuView;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.ArrivalAdapter;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SimpleRouteArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArrivalListFragment extends BaseFragment
        implements ArrivalListView, ActiveTrackingMenuView {

    public interface ArrivalListListener {
        void onArrivalClicked(ArrivalModel arrivalModel);
    }

    @Inject
    ArrivalListPresenter arrivalListPresenter;

    @Bind(R.id.rv_arrival_list)
    RecyclerView rvArrivals;

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

//    @Bind(R.id.tv_arrival_list_last_updated)
//    TextView tvLastUpdated;

    @Bind(R.id.ib_arrival_list_favorite)
    ImageButton ibFavorite;

    @Bind(R.id.ib_arrival_list_map)
    ImageButton ibViewMap;

    @Bind(R.id.ib_arrival_list_notifications)
    ImageButton ibNotification;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Bind(R.id.layout_save_stop)
    LinearLayout layoutSaveStop;

    @Bind(R.id.layout_save_stop_new_group)
    LinearLayout layoutCreateNewGroup;

    @Bind(R.id.sp_save_stop_groups)
    Spinner spGroups;

    @Bind(R.id.et_save_stop_new_group_name)
    EditText etNewGroupName;

    @Bind(R.id.btn_save_stop_save)
    Button btnSaveStop;

    @Bind(R.id.layout_enable_active_tracking)
    LinearLayout layoutActiveTracking;

    @Bind(R.id.sp_active_tracking_routes)
    Spinner spActiveTrackingRoutes;

    @Bind(R.id.sp_active_tracking_duration)
    Spinner spActiveTrackingDuration;

    @Bind(R.id.sp_active_tracking_vehicle_distance)
    Spinner spActiveTrackingVehicleDistance;

    @Bind(R.id.tv_active_tracking_routes_loading)
    TextView tvActiveTrackingRoutesLoading;

    @Bind(R.id.tv_active_tracking_routes_error)
    TextView tvActiveTrackingRoutesError;

    @Bind(R.id.cb_active_tracking_enable_ring)
    CheckBox cbEnableRing;

    @Bind(R.id.cb_active_tracking_enable_vibrate)
    CheckBox cbEnableVibrate;

    //TODO move headers layout to below active tracking view if showing

    private ArrivalAdapter arrivalsAdapter;
    private LinearLayoutManager arrivalsLayoutManager;

    private ArrivalListListener arrivalListListener;

    private static final String ARGUMENT_KEY_STOP_ID = "com.tamerbarsbay.depothouston.ARGUMENT_STOP_ID";
    private static final String ARGUMENT_KEY_STOP_NAME = "com.tamerbarsbay.depothouston.ARGUMENT_STOP_NAME";
    private String stopId;
    private String stopName;

    /**
     * When the user wants to favorite a stop, they are presented with a list of groups they can
     * put the stop into. This String is used as an item in the list in case the user wants to
     * create a new group to put the stop into.
     */
    private static final String CREATE_NEW_GROUP = "+ New Group";

    public ArrivalListFragment() {}

    /**
     * Create a new instance of an ArrivalListFragment using the id of the stop for which
     * to pull arrivals.
     * @param stopId The id of the stop for which we are loading arrivals.
     * @return New ArrivalListFragment object.
     */
    public static ArrivalListFragment newInstance(String stopId, String stopName) {
        ArrivalListFragment fragment = new ArrivalListFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_KEY_STOP_ID, stopId);
        args.putString(ARGUMENT_KEY_STOP_NAME, stopName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ArrivalListListener) {
            arrivalListListener = (ArrivalListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_arrival_list, container, false);
        ButterKnife.bind(this, fragmentView);

        arrivalsLayoutManager = new LinearLayoutManager(getContext());
        rvArrivals.setLayoutManager(arrivalsLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadArrivalList();
            }
        });

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadArrivalList();
    }

    @Override
    public void onResume() {
        super.onResume();
        arrivalListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        arrivalListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arrivalListPresenter.destroy();
    }

    private void initialize() {
        getComponent(ArrivalComponent.class).inject(this);
        arrivalListPresenter.setViews(this, this);
        if (getArguments() != null) {
            stopId = getArguments().getString(ARGUMENT_KEY_STOP_ID, null);
            stopName = getArguments().getString(ARGUMENT_KEY_STOP_NAME, null);
            if (stopId == null || stopName == null) {
                return;
            }
            RecentStopUtils.addRecentStop(getContext(), new RecentStopModel(stopId, stopName));
            setFavoriteIcon();
        }
    }

    @Override
    public void renderArrivalList(Collection<ArrivalModel> arrivalModels) {
        if (arrivalModels != null) {
            if (arrivalsAdapter == null) {
                arrivalsAdapter = new ArrivalAdapter(getActivity(), arrivalModels);
            } else {
                arrivalsAdapter.setArrivalsCollection(arrivalModels);
            }
            arrivalsAdapter.setOnItemClickListener(onItemClickListener);
            rvArrivals.setAdapter(arrivalsAdapter);
        }
    }

    /**
     * Set the favorite icon to a hollow star if this stop is not saved yet,
     * or a full star if it is.
     */
    private void setFavoriteIcon() {
        if (stopId == null) {
            return;
        }
        if (SavedStopUtils.isStopSaved(getContext(), stopId)) {
            ibFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
        } else {
            ibFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
        }
    }

    @Override
    public void showLoadingView() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
    }

    @Override
    public void hideLoadingView() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showRetryView() {
        rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {
        rlRetry.setVisibility(View.GONE);
    }

    @Override public void showError(String message) {
        showToastMessage(message);
    }

    private void loadArrivalList() {
        arrivalListPresenter.loadArrivalList();
    }

    private void populateSavedGroupsSpinner() {
        if (spGroups != null) {
            ArrayList<String> groupNames = SavedStopUtils.getGroupNamesArray(getContext());
            groupNames.add(CREATE_NEW_GROUP);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), R.layout.list_item_simple_white, R.id.tv_simple_list_item_name, groupNames);
            adapter.setDropDownViewResource(R.layout.list_item_simple);
            spGroups.setAdapter(adapter);
        }
    }

    private void hideSaveMenu() {
        if (layoutSaveStop.getVisibility() == View.VISIBLE) {
            layoutSaveStop.setVisibility(View.GONE);
        }
    }

    private void showSaveMenu() {
        if (layoutSaveStop.getVisibility() == View.GONE) {
            layoutSaveStop.setVisibility(View.VISIBLE);
        }
    }

    private void showActiveTrackingMenu() {
        if (layoutActiveTracking.getVisibility() == View.GONE) {
            layoutActiveTracking.setVisibility(View.VISIBLE);
            //TODO move headers to below this
        }
    }

    private void hideActiveTrackingMenu() {
        if (layoutActiveTracking.getVisibility() == View.VISIBLE) {
            layoutActiveTracking.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        loadArrivalList();
    }

    @OnClick(R.id.ib_arrival_list_favorite)
    void onFavoriteIconClicked() {
        if (layoutSaveStop.getVisibility() == View.GONE) {
            hideActiveTrackingMenu();
            showSaveMenu();
            populateSavedGroupsSpinner();
        }
    }

    @OnClick(R.id.ib_arrival_list_notifications)
    void onActiveTrackingMenuClicked() {
        if (layoutActiveTracking.getVisibility() == View.GONE) {
            hideSaveMenu();
            showActiveTrackingMenu();
            populateActiveTrackingDurationOptions(new ArrayList<String>(Arrays.asList(DURATION_OPTIONS)));
            populateActiveTrackingVehicleDistanceOptions(new ArrayList<Integer>(Arrays.asList(VEHICLE_DISTANCE_OPTIONS)));
            if (arrivalListPresenter != null && stopId != null) {
                Log.d("ArrivalListFragment", "presenter.loadRoutesByStop"); //TODO temp log
                arrivalListPresenter.loadRoutesByStop(stopId);
            }
        }
    }

    @OnClick(R.id.btn_save_stop_save)
    void onSaveStopClicked() {
        String groupName;
        if (spGroups.getSelectedItem().toString().equals(CREATE_NEW_GROUP)) {
            // User is creating new group
            groupName = etNewGroupName.getText().toString().trim();
        } else {
            // User is adding to existing group
            groupName = spGroups.getSelectedItem().toString();
        }

        if (groupName.length() > 0 && stopId != null && stopName != null) {
            btnSaveStop.setEnabled(false);
            SavedStopUtils.saveStopToGroup(
                    getContext(),
                    groupName,
                    new SavedStopModel(0, stopId, stopName));
            hideSaveMenu();
            showSnackbar(getView(), getString(R.string.stop_saved_to_favorites));
        }
    }

    @OnClick(R.id.btn_save_stop_cancel)
    void onCancelSaveStopClicked() {
        spGroups.setSelection(0);
        hideSaveMenu();
    }

    @OnClick(R.id.btn_active_tracking_cancel)
    void onCancelActiveTrackingClicked() {
        resetActiveTrackingOptions();
        hideActiveTrackingMenu();
    }

    @OnItemSelected(R.id.sp_save_stop_groups)
    void onSavedGroupSelected(int position) {
        int lastSpinnerPosition = spGroups.getAdapter().getCount()-1;
        if (position == lastSpinnerPosition) {
            etNewGroupName.clearComposingText();
            btnSaveStop.setEnabled(false);
            layoutCreateNewGroup.setVisibility(View.VISIBLE);
        } else {
            layoutCreateNewGroup.setVisibility(View.GONE);
            etNewGroupName.clearComposingText();
            btnSaveStop.setEnabled(true);
        }
    }

    @OnTextChanged(R.id.et_save_stop_new_group_name)
    void onNewGroupNameTextChanged(CharSequence text) {
        btnSaveStop.setEnabled(text.toString().trim().length() > 0);
    }

    @OnClick(R.id.fab_arrival_list_refresh)
    void onRefreshClicked() {
        loadArrivalList();
    }

    @Override
    public void populateActiveTrackingRouteOptions(Collection<RouteModel> routes) {
        spActiveTrackingRoutes.setVisibility(View.VISIBLE);
        SimpleRouteArrayAdapter adapter = new SimpleRouteArrayAdapter(
                getContext(),
                R.layout.list_item_simple_white,
                R.id.tv_simple_list_item_name,
                routes);
        adapter.setDropDownViewResource(R.layout.list_item_simple);
        spActiveTrackingRoutes.setAdapter(adapter);
    }

    @Override
    public void populateActiveTrackingDurationOptions(Collection<String> durationOptions) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(),
                R.layout.list_item_simple_white,
                R.id.tv_simple_list_item_name,
                (List<String>) durationOptions);
        adapter.setDropDownViewResource(R.layout.list_item_simple);
        if (spActiveTrackingDuration != null) {
            spActiveTrackingDuration.setAdapter(adapter);
        }
    }

    @Override
    public void populateActiveTrackingVehicleDistanceOptions(Collection<Integer> vehicleDistanceOptions) {
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(
                getContext(),
                R.layout.list_item_simple_white,
                R.id.tv_simple_list_item_name,
                (List<Integer>) vehicleDistanceOptions);
        adapter.setDropDownViewResource(R.layout.list_item_simple);
        if (spActiveTrackingVehicleDistance != null) {
            spActiveTrackingVehicleDistance.setAdapter(adapter);
            spActiveTrackingVehicleDistance.setSelection(DEFAULT_VEHICLE_DISTANCE_INDEX);
        }
    }

    @OnClick(R.id.btn_active_tracking_enable)
    void onEnableActiveTrackingPressed() {
        RouteModel route = (RouteModel) spActiveTrackingRoutes.getSelectedItem();
        String trackingDuration = (String) spActiveTrackingDuration.getSelectedItem();
        int vehicleDistance = (Integer) spActiveTrackingVehicleDistance.getSelectedItem();
        boolean enableRing = cbEnableRing.isChecked();
        boolean enableVibrate = cbEnableVibrate.isChecked();
        enableActiveTrackingService(route.getRouteId(), route.getRouteName(), stopId, stopName,
                trackingDuration, vehicleDistance, enableVibrate, enableRing);
        resetActiveTrackingOptions();
    }

    private void resetActiveTrackingOptions() {
        spActiveTrackingRoutes.setSelection(0);
        spActiveTrackingDuration.setSelection(0);
        spActiveTrackingVehicleDistance.setSelection(0);
        cbEnableRing.setChecked(false);
        cbEnableVibrate.setChecked(false);
    }

    @Override
    public void enableActiveTrackingService(String routeId, String routeNum,
                                            String stopId, String stopName,
                                            String trackingDurationMins, int vehicleDistanceMins,
                                            boolean isVibrateEnabled, boolean isRingEnabled) {
        int trackingDurationMillis = Integer.parseInt(
                trackingDurationMins.substring(0, trackingDurationMins.indexOf(" "))) * 60000;
        long endUpdatesTimeMillis = System.currentTimeMillis() + trackingDurationMillis;

        // The unique id which will be assigned to the notifications being issued
        int uniqueId = stopId.hashCode();

        //TODO change to snackbar?
        Toast.makeText(getContext(),
                "Active tracking enabled for " + trackingDurationMins + " - swipe away to cancel.",
                Toast.LENGTH_LONG).show();

        Intent intent = ActiveTrackingService.getCallingIntent(getContext(), routeId,
                routeNum, stopId, stopName, uniqueId, vehicleDistanceMins, endUpdatesTimeMillis, isRingEnabled, isVibrateEnabled);
        getContext().startService(intent); //TODO move to activity?

        hideActiveTrackingMenu();
        resetActiveTrackingOptions();
    }

    @Override
    public void showActiveTrackingRoutesErrorView() {
        if (spActiveTrackingRoutes.getVisibility() == View.VISIBLE) {
            spActiveTrackingRoutes.setVisibility(View.GONE);
        }
        tvActiveTrackingRoutesError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideActiveTrackingRoutesErrorView() {
        tvActiveTrackingRoutesError.setVisibility(View.GONE);
    }

    @Override
    public void showActiveTrackingRoutesLoadingView() {
        if (spActiveTrackingRoutes.getVisibility() == View.VISIBLE) {
            spActiveTrackingRoutes.setVisibility(View.GONE);
        }
        tvActiveTrackingRoutesLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideActiveTrackingRoutesLoadingView() {
        tvActiveTrackingRoutesLoading.setVisibility(View.GONE);
    }

    private ArrivalAdapter.OnItemClickListener onItemClickListener =
            new ArrivalAdapter.OnItemClickListener() {
                @Override
                public void onArrivalItemClicked(ArrivalModel arrivalModel) {
                    if (ArrivalListFragment.this.arrivalListPresenter != null && arrivalModel != null) {
                        //ArrivalListFragment.this.arrivalListPresenter.onArrivalClicked(arrivalModel);
                        //TODO necessary?
                    }
                }
            };

}
