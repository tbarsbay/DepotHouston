package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.MapSearchPresenter;
import com.tamerbarsbay.depothouston.presentation.util.UserLocationManager;
import com.tamerbarsbay.depothouston.presentation.view.MapSearchView;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MapSearchActivity extends NavigationDrawerActivity implements MapSearchView,
        UserLocationManager.UserLocationListener {

    @Bind(R.id.layout_map_search_parent)
    LinearLayout layoutParent;

    @Inject
    MapSearchPresenter mapSearchPresenter;

    private GoogleMap mMap;
    private UserLocationManager userLocationManager;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 100;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MapSearchActivity.class);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MAP_SEARCH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);
        ButterKnife.bind(this);
        setupMapIfNeeded();
        //TODO setup userlocationmanager
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }

    private void setupMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                //TODO set map on info window click
                //TODO set map info window adapter
                //TODO get and plot nearby stops
            }
        }
    }

    private UserLocationManager getUserLocationManager() {
        return userLocationManager != null ?
                userLocationManager :
                new UserLocationManager(this, this);
    }

    private void showPermissionRationale() {
        if (layoutParent != null) {
            Snackbar
                    .make(layoutParent, R.string.location_permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.OK, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(
                                    MapSearchActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    })
                    .show();
        }
    }

    private void getUserLocationAndLoadNearbyStops() {
        Location userLocation = getUserLocationManager().getUserLocation();
        //TODO load nearby stops
    }

    @Override
    public void renderStopList(Collection<StopModel> stopModels) {
        //TODO plot all stops
    }

    @Override
    public void viewStop(StopModel stopModel) {
        //TODO handle inactive routes/stops correctly
        if (stopModel != null) {
            navigator.navigateToArrivalList(this, stopModel.getStopId());
        }
    }

    @Override
    public void onConnected() {
        if (Build.VERSION.SDK_INT < 23) {
            // User has Lollipop or below, no need to request permissions
            // Just get user location and load nearby stops
            getUserLocationAndLoadNearbyStops();
        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // User has not granted location permission
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showPermissionRationale();
                }
            } else {
                // User has granted location permission
                getUserLocationAndLoadNearbyStops();
            }
        }
        //TODO if not granted, show error and "you can still tap on map to see stops near any location
    }

    @Override
    public void onConnectionFailed() {
        showError(getString(R.string.error_invalid_user_location_tap_feature_remains));
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void hideLoading() {
        //TODO
    }

    @Override
    public void hideRetry() {
        //TODO
    }

    @Override
    public void showError(String message) {
        if (layoutParent != null) {
            //TODO
        }
    }

    @Override
    public void showLoading() {
        //TODO
    }

    @Override
    public void showRetry() {
        //TODO
    }
}
