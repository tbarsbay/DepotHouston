package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.StopComponent;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.StopListPresenter;
import com.tamerbarsbay.depothouston.presentation.util.PrefUtils;
import com.tamerbarsbay.depothouston.presentation.util.UserLocationManager;
import com.tamerbarsbay.depothouston.presentation.view.StopListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.StopAdapter;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StopListFragment extends BaseFragment
        implements StopListView, UserLocationManager.UserLocationListener {

    public interface StopListListener {
        void onStopClicked(final StopModel stopModel);
        StopComponent getStopComponent();
    }

    @Inject
    StopListPresenter stopListPresenter;

    @Bind(R.id.rv_stop_list)
    RecyclerView rvStops;

    @Bind(R.id.layout_stop_list_parent)
    RelativeLayout layoutParent;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Bind(R.id.layout_stop_list_empty)
    LinearLayout layoutEmpty;

    private StopAdapter stopsAdapter;
    private LinearLayoutManager stopsLayoutManager;

    private StopListListener stopListListener;

    private UserLocationManager userLocationManager;

    private SwitchCompat nearbyToggle;

    private String routeId;
    private String direction;

    private static final String ARGUMENT_KEY_ROUTE_ID = "com.tamerbarsbay.depothouston.ARGUMENT_ROUTE_ID";
    private static final String ARGUMENT_KEY_DIRECTION = "com.tamerbarsbay.depothouston.ARGUMENT_DIRECTION";

    public StopListFragment() {}

    /**
     * Create a new instance of a StopListFragment using the id of the route for which
     * to pull stops.
     * @param routeId The id of the route for which we are loading stops.
     * @return New StopListFragment object.
     */
    public static StopListFragment newInstance(String routeId, String direction) {
        StopListFragment fragment = new StopListFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_KEY_ROUTE_ID, routeId);
        args.putString(ARGUMENT_KEY_DIRECTION, direction);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        rvStops.setLayoutManager(new LinearLayoutManager(getContext()));
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();

        //TODO this seems iffy, how will it handle permission revocation?
        if (PrefUtils.isFilterNearbyRoutesEnabled(getContext())) {
            getUserLocationAndLoadNearbyStops();
        } else {
            loadStopList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
                // If the request was cancelled, the results array is empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Granted, get the user's location and show nearby stops
                    getUserLocationAndLoadNearbyStops();
                } else {
                    // Denied, reset the nearby toggle
                    resetNearbyToggle();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.findItem(R.id.action_filter_nearby);
        if (item != null) {
            nearbyToggle = (SwitchCompat) MenuItemCompat.getActionView(item);
            styleNearbyToggle();
            nearbyToggle.setOnCheckedChangeListener(onNearbyStopsToggledListener);
            nearbyToggle.setChecked(PrefUtils.isFilterNearbyRoutesEnabled(getContext()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter_nearby) {
            // User wants to filter for only nearby stops
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void styleNearbyToggle() {
        nearbyToggle.setText(getString(R.string.nearby_only));
        nearbyToggle.setTypeface(null, Typeface.BOLD);
        nearbyToggle.setTextColor(getResources().getColor(R.color.white));

        int sizeInDp = 16;
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (sizeInDp*scale + 0.5f);
        nearbyToggle.setSwitchPadding(dpAsPixels);
    }

    private void onNearbyStopsToggled(boolean isChecked) {
        PrefUtils.setFilterNearbyRoutesEnabled(getContext(), isChecked);
        if (isChecked) {
            getUserLocationManager().connect();
        } else {
            getUserLocationManager().disconnect();
            loadStopList();
        }
    }

    private void getUserLocationAndLoadNearbyStops() {
        Location userLocation = getUserLocationManager().getUserLocation();

//        //TODO temp for testing purposes
//        userLocation.setLatitude(29.791032);
//        userLocation.setLongitude(-95.404918);

        if (userLocation != null) {
            loadNearbyStopList(
                    routeId,
                    userLocation.getLatitude(),
                    userLocation.getLongitude(),
                    PrefUtils.getNearbyThresholdInMiles(getContext()));
        } else {
            showError(getString(R.string.error_invalid_user_location_showing_all_stops));
            resetNearbyToggle();
        }
    }

    private UserLocationManager getUserLocationManager() {
        if (userLocationManager == null) {
            userLocationManager = new UserLocationManager(getContext(), this);
        }
        return userLocationManager;
    }

    @Override
    public void onConnected() {
        if (Build.VERSION.SDK_INT < 23) {
            // User has Lollipop or below, no need to request permissions
            // Just get user location and load nearby stops
            getUserLocationAndLoadNearbyStops();
        } else {
            if (!hasUserGrantedLocationPermission()) {
                // User has not granted location permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionRationale(layoutParent, R.string.location_permission_rationale_nearby_stops);
                } else {
                    showSnackbar(layoutParent, R.string.enable_location_permission_to_see_nearby_stops);
                    resetNearbyToggle();
                }
            } else {
                // User has granted location permission
                getUserLocationAndLoadNearbyStops();
            }
        }
    }

    @Override
    public void onConnectionFailed() {
        showError(getString(R.string.error_invalid_user_location_showing_all_stops));
        resetNearbyToggle();
    }

    private void resetNearbyToggle() {
        if (nearbyToggle != null) {
            PrefUtils.setFilterNearbyRoutesEnabled(getContext(), false);
            nearbyToggle.setChecked(false);
        }
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
        if (stopListListener != null) {
            stopListListener.getStopComponent().inject(this);
        }
        stopListPresenter.setView(this);
        if (getArguments() != null) {
            routeId = getArguments().getString(ARGUMENT_KEY_ROUTE_ID, null);
            direction = getArguments().getString(ARGUMENT_KEY_DIRECTION, null);
        }
    }

    @Override
    public void renderStopList(Collection<StopModel> stopModels) {
        if (stopModels != null) {
            if (stopsAdapter == null) {
                stopsAdapter = new StopAdapter(getActivity(), stopModels);
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
    public void showEmptyView() {
        layoutEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        layoutEmpty.setVisibility(View.GONE);
    }

    @Override public void showError(String message) {
        showSnackbarError(layoutParent, message);
    }

    private void loadStopList() {
        stopListPresenter.loadStopList();
    }

    private void loadNearbyStopList(String routeId, double lat, double lon, String radiusInMiles) {
        if (routeId == null || radiusInMiles == null) {
            showError(getString(R.string.error_loading_nearby_stops));
            return;
        }
        stopListPresenter.loadNearbyStopsByRoute(routeId, lat, lon, radiusInMiles);
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        stopListPresenter.retryLastRequest();
    }

    private StopAdapter.OnItemClickListener onItemClickListener =
            new StopAdapter.OnItemClickListener() {
                @Override
                public void onStopItemClicked(StopModel stopModel) {
                    if (StopListFragment.this.stopListPresenter != null && stopModel != null) {
                        StopListFragment.this.stopListPresenter.onStopClicked(stopModel);
                    }
                }
            };

    private CompoundButton.OnCheckedChangeListener onNearbyStopsToggledListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onNearbyStopsToggled(isChecked);
                }
            };
}
