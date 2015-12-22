package com.tamerbarsbay.depothouston.presentation.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.MapSearchComponent;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.MapSearchPresenter;
import com.tamerbarsbay.depothouston.presentation.util.UserLocationManager;
import com.tamerbarsbay.depothouston.presentation.view.DividerItemDecoration;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.NearbyStopAdapter;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapSearchFragment
    extends BaseFragment implements UserLocationManager.UserLocationListener, MapSearchView {

    @Bind(R.id.rv_map_search_stops)
    RecyclerView rvStops;

    @Bind(R.id.tv_map_search_empty)
    TextView tvEmptyStops;

    @Bind(R.id.tv_map_search_prompt)
    TextView tvInitialPrompt;

    @Bind(R.id.tv_map_search_center_address)
    TextView tvCenterAddress;

    @Bind(R.id.layout_map_search_header)
    LinearLayout layoutStopsHeader;

    @Bind(R.id.layout_map_search_parent)
    RelativeLayout layoutParent;

    @Bind(R.id.layout_progress)
    RelativeLayout layoutProgress;

    @Bind(R.id.layout_map_search_retry)
    LinearLayout layoutRetry;

    @Inject
    MapSearchPresenter mapSearchPresenter;

    private UserLocationManager userLocationManager;

    private MapSearchListener mapSearchListener;

    private NearbyStopAdapter adapter;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;
    public static final String DEFAULT_RADIUS_MILES = ".25"; //TODO get from user settings

    private static final double HOUSTON_SOUTHWEST_LAT = 29.431005;
    private static final double HOUSTON_SOUTHWEST_LON = -95.885714;
    private static final double HOUSTON_NORTHEAST_LAT = 30.204589;
    private static final double HOUSTON_NORTHEAST_LON = -94.871179;
    private static final double HOUSTON_CENTER_LAT = 29.760215;
    private static final double HOUSTON_CENTER_LON =  -95.370019;

    /**
     * Coordinates for slightly outside the Houston borders, used for centering the map on Houston
     */
    private static final LatLngBounds HOUSTON_BOUNDS = new LatLngBounds(
            new LatLng(HOUSTON_SOUTHWEST_LAT, HOUSTON_SOUTHWEST_LON),
            new LatLng(HOUSTON_NORTHEAST_LAT, HOUSTON_NORTHEAST_LON)
    );

    public static MapSearchFragment newInstance() {
        return new MapSearchFragment();
    }

    public MapSearchFragment() {}

    public interface MapSearchListener {
        /**
         * Plot a collection of transit stops on the map using markers.
         * @param stops
         */
        void renderStopList(Collection<StopModel> stops);

        /**
         * Handle the user tapping on a specific transit stop. This will normally open up the
         * arrivals screen for that stop.
         * @param stop
         */
        void viewStop(StopModel stop);

        /**
         * Clear the map of all markers and polylines.
         */
        void clearMap();

        /**
         * Plot a marker on the map to designate the current center location around which
         * nearby stops are being shown.
         * @param lat
         * @param lon
         */
        void plotCenterMarker(double lat, double lon);

        /**
         * Center the map on a specific location.
         * @param location
         */
        void centerMapOn(LatLng location, int zoomLevel);

        /**
         * Expand the map's size on the screen when there are no stops to show in the list view.
         */
        void expandMapView();

        /**
         * Shrink the map's size on the screen when there is a list of stops to show to the user.
         */
        void collapseMapView();
    }

    private UserLocationManager getUserLocationManager() {
        if (userLocationManager == null) {
            userLocationManager = new UserLocationManager(getContext(), this);
        }
        return userLocationManager;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapSearchListener) {
            mapSearchListener = (MapSearchListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_map_search, container, false);
        ButterKnife.bind(this, fragmentView);
        setupUI();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(MapSearchComponent.class).inject(this);
        mapSearchPresenter.setView(this);
        getUserLocationManager();
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
                    // Denied, center on Houston
                    centerMapOnHouston();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapSearchPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapSearchPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapSearchPresenter != null) {
            mapSearchPresenter.destroy();
        }
        if (userLocationManager != null) {
            userLocationManager.disconnect();
        }
    }

    private void setupUI() {
        rvStops.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NearbyStopAdapter(getContext(), new ArrayList<StopModel>());
        adapter.setOnItemClickListener(onStopClickedListener);
        rvStops.setAdapter(adapter);
        rvStops.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    private void showEnablePermissionMessage() {
        if (layoutParent != null) {
            Snackbar snackbar = Snackbar
                    .make(layoutParent,
                            R.string.enable_location_permission_to_see_nearby_stops,
                            Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_default));
            snackbar.show();
        }
    }

    private void getUserLocationAndLoadNearbyStops() {
        Location userLocation = getUserLocationManager().getUserLocation();

        if (userLocation != null) {
            LatLng userLocationLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
            if (HOUSTON_BOUNDS.contains(userLocationLatLng)) {
                // User is in Houston area, center on them
                getNearbyStops(
                        getString(R.string.your_location),
                        userLocation.getLatitude(),
                        userLocation.getLongitude(),
                        DEFAULT_RADIUS_MILES);
                return;
            }
        } else {
            showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
        }

        // Center the map on Houston by default (in case user location is null or outside Houston)
        centerMapOnHouston();
    }

    private void centerMapOnHouston() {
        centerMapOn(HOUSTON_CENTER_LAT, HOUSTON_CENTER_LON, ZOOM_LEVEL_FAR);
    }

    public void getNearbyStops(String centerAddress, double lat, double lon, String radiusInMiles) {
        // Hide the initial user prompt as soon as a search is mad
        if (tvInitialPrompt != null && tvInitialPrompt.getVisibility() == View.VISIBLE) {
            tvInitialPrompt.setVisibility(View.GONE);
        }
        if (mapSearchPresenter != null && adapter != null) {
            adapter.setCenterLocation(new LatLng(lat, lon));
            mapSearchPresenter.initialize(centerAddress, lat, lon, radiusInMiles);
        }
    }

    private void showPermissionRationale() {
        if (layoutParent != null) {
            Snackbar snackbar = Snackbar
                    .make(layoutParent, R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.OK, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    })
                    .setActionTextColor(getResources().getColor(R.color.white));
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_default));
            snackbar.show();
        }
    }

    @Override
    public void onConnected() {
        if (Build.VERSION.SDK_INT < 23) {
            // User has Lollipop or below, no need to request permissions
            // Just get user location and load nearby stops
            getUserLocationAndLoadNearbyStops();
        } else {
            if (ActivityCompat.checkSelfPermission(
                    getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // User has not granted location permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionRationale();
                } else {
                    showEnablePermissionMessage();
                    centerMapOnHouston();
                }
            } else {
                // User has granted location permission
                getUserLocationAndLoadNearbyStops();
            }
        }
    }

    @Override
    public void onConnectionFailed() {
        showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
    }

    @Override
    public void clearMap() {
        if (mapSearchListener != null) {
            mapSearchListener.clearMap();
        }
    }

    @Override
    public void centerMapOn(double lat, double lon, int zoomLevel) {
        if (mapSearchListener != null) {
            mapSearchListener.centerMapOn(new LatLng(lat, lon), zoomLevel);
        }
    }

    @Override
    public void renderStopList(Collection<StopModel> stopModels) {
        if (stopModels != null) {
            if (stopModels.isEmpty()) {
                hideStopsView();
                showEmptyView();
                adapter.setStopsCollection(new ArrayList<StopModel>());
                return;
            } else {
                showStopsView();
                hideEmptyView();
            }
            adapter.setStopsCollection(stopModels);
            if (mapSearchListener != null) {
                mapSearchListener.renderStopList(stopModels);
            }
        }
    }

    @Override
    public void plotCenterMarker(String centerAddress, double lat, double lon) {
        if (tvCenterAddress != null) {
            tvCenterAddress.setText(centerAddress);
        }
        if (mapSearchListener != null) {
            mapSearchListener.plotCenterMarker(lat, lon);
        }
    }

    @Override
    public void viewStop(StopModel stopModel) {
        if (mapSearchListener != null) {
            mapSearchListener.viewStop(stopModel);
        }
    }

    @Override
    public void showError(String message) {
        if (layoutParent != null) {
            Snackbar snackbar = Snackbar.make(layoutParent, message, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.snackbar_error));
            snackbar.show();
        }
    }

    @Override
    public void showLoadingView() {
        if (layoutProgress != null) {
            layoutProgress.setVisibility(View.VISIBLE);
            expandMapView();
        }
    }

    @Override
    public void hideLoadingView() {
        if (layoutProgress != null) {
            layoutProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showStopsView() {
        if (rvStops != null && layoutStopsHeader != null) {
            layoutStopsHeader.setVisibility(View.VISIBLE);
            rvStops.setVisibility(View.VISIBLE);
            collapseMapView();
        }
    }

    @Override
    public void hideStopsView() {
        if (rvStops != null && layoutStopsHeader != null) {
            layoutStopsHeader.setVisibility(View.GONE);
            rvStops.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRetryView() {
        if (layoutRetry != null) {
            layoutRetry.setVisibility(View.VISIBLE);
            expandMapView();
        }
    }

    @Override
    public void hideRetryView() {
        if (layoutRetry != null) {
            layoutRetry.setVisibility(View.GONE);
        }
    }

    @Override
    public void showEmptyView() {
        if (tvEmptyStops != null) {
            tvEmptyStops.setVisibility(View.VISIBLE);
            expandMapView();
        }
    }

    @Override
    public void hideEmptyView() {
        if (tvEmptyStops != null) {
            tvEmptyStops.setVisibility(View.GONE);
        }
    }

    @Override
    public void expandMapView() {
        if (mapSearchListener != null) {
            mapSearchListener.expandMapView();
        }
    }

    @Override
    public void collapseMapView() {
        if (mapSearchListener != null) {
            mapSearchListener.collapseMapView();
        }
    }

    @OnClick(R.id.layout_map_search_retry)
    void onRetryClicked() {
        if (mapSearchPresenter != null) {
            mapSearchPresenter.initializePreviousRequest();
        }
    }

    private NearbyStopAdapter.OnItemClickListener onStopClickedListener =
            new NearbyStopAdapter.OnItemClickListener() {
                @Override
                public void onStopClicked(StopModel stopModel) {
                    if (stopModel != null && mapSearchListener != null) {
                        mapSearchListener.viewStop(stopModel);
                    }
                }
            };

}
