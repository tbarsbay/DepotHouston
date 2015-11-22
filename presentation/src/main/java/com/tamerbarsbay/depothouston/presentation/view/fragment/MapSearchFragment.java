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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.MapSearchComponent;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.MapSearchPresenter;
import com.tamerbarsbay.depothouston.presentation.util.UserLocationManager;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.NearbyStopAdapter;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapSearchFragment
    extends BaseFragment implements UserLocationManager.UserLocationListener, MapSearchView {

    @Bind(R.id.rv_map_search_stops)
    RecyclerView rvStops;

    @Bind(R.id.tv_map_search_empty)
    TextView tvEmptyStops;

    @Bind(R.id.tv_map_search_prompt)
    TextView tvInitialPrompt;

    @Bind(R.id.layout_map_search_parent)
    RelativeLayout layoutParent;

    @Bind(R.id.layout_progress)
    RelativeLayout layoutProgress;

    @Inject
    MapSearchPresenter mapSearchPresenter;

    private UserLocationManager userLocationManager;

    private MapSearchListener mapSearchListener;

    private NearbyStopAdapter adapter;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;
    public static final String DEFAULT_RADIUS_MILES = ".25"; //TODO get from user settings

    // Coordinates for slightly outside the Houston borders
    private static final double HOUSTON_SOUTHWEST_LAT = 29.393960;
    private static final double HOUSTON_SOUTHWEST_LON = -95.083066;
    private static final double HOUSTON_NORTHEAST_LAT = 30.204589;
    private static final double HOUSTON_NORTHEAST_LON = -94.871179;
    private static final double HOUSTON_CENTER_LAT = 29.760215;
    private static final double HOUSTON_CENTER_LON =  -95.370019;
    private static final LatLngBounds HOUSTON_BOUNDS = new LatLngBounds(
            new LatLng(HOUSTON_SOUTHWEST_LAT, HOUSTON_SOUTHWEST_LON),
            new LatLng(HOUSTON_NORTHEAST_LAT, HOUSTON_NORTHEAST_LON)
    );

    public static MapSearchFragment newInstance() {
        return new MapSearchFragment();
    }

    public MapSearchFragment() {}

    public interface MapSearchListener {
        void renderStopList(Collection<StopModel> stops);
        void viewStop(StopModel stop);
        void clearMap();
        void plotCenterMarker(double lat, double lon);
        void centerMapOn(LatLng location);
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
            Log.d("MapSearchFragment", "user location not null"); //TODO temp log
            // If the user is in Houston, center on their location. If the user is outside of
            // Houston, center on Houston.
            if (HOUSTON_BOUNDS.contains(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))) {
                // User is in Houston area
                getNearbyStops(userLocation.getLatitude(), userLocation.getLongitude(), DEFAULT_RADIUS_MILES);
            } else {
                // User is not in the Houston area - center the map on Houston.
                if (mapSearchListener != null) {
                    mapSearchListener.centerMapOn(new LatLng(HOUSTON_CENTER_LAT, HOUSTON_CENTER_LON));
                }
            }
        } else {
            Log.d("MapSearchFragment", "user location null"); //TODO temp log
            showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
        }
    }

    public void getNearbyStops(double lat, double lon, String radiusInMiles) {
        // Hide the initial user prompt as soon as a search is mad
        if (tvInitialPrompt != null && tvInitialPrompt.getVisibility() == View.VISIBLE) {
            tvInitialPrompt.setVisibility(View.GONE);
        }
        if (mapSearchPresenter != null && adapter != null) {
            adapter.setCenterLocation(new LatLng(lat, lon));
            mapSearchPresenter.initialize(lat, lon, radiusInMiles);
        } else {
        }
    }

    private void showPermissionRationale() {
        if (layoutParent != null) {
            Snackbar snackbar = Snackbar
                    .make(layoutParent, R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.OK, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(
                                    getActivity(),
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
                Log.d("MapSearch", "User has not granted permission"); //TODO temp logs
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.d("MapSearch", "Should show permission rationale");
                    showPermissionRationale();
                } else {
                    Log.d("MapSearch", "Should not show permission rationale");
                    showEnablePermissionMessage();
                }
            } else {
                Log.d("MapSearch", "User has granted permission");
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
    public void renderStopList(Collection<StopModel> stopModels) {
        if (stopModels != null) {
            if (stopModels.isEmpty()) {
                tvEmptyStops.setVisibility(View.VISIBLE);
                rvStops.setVisibility(View.GONE);
                adapter.setStopsCollection(new ArrayList<StopModel>());
                return;
            } else {
                rvStops.setVisibility(View.VISIBLE);
                tvEmptyStops.setVisibility(View.GONE);
            }
            adapter.setStopsCollection(stopModels);
            if (mapSearchListener != null) {
                Log.d("MapSearchFragment", "listener not null"); //TODO temp
                mapSearchListener.renderStopList(stopModels);
            } else {
                Log.d("MapSearchFragment", "listener null"); //TODO temp
            }
        } else {
            Log.d("MapSearchFragment", "stop models null"); //TODO temp
        }
    }

    @Override
    public void plotCenterMarker(double lat, double lon) {
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
    public void showLoading() {
        if (layoutProgress != null) {
            layoutProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (layoutProgress != null) {
            layoutProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showStopsView() {
        if (rvStops != null) {
            rvStops.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideStopsView() {
        if (rvStops != null) {
            rvStops.setVisibility(View.GONE);
        }
    }

    @Override
    public void showRetry() {
        //TODO
    }

    @Override
    public void hideRetry() {
        //TODO
    }

    @Override
    public void showEmpty() {
        if (tvEmptyStops != null) {
            tvEmptyStops.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmpty() {
        if (tvEmptyStops != null) {
            tvEmptyStops.setVisibility(View.GONE);
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
