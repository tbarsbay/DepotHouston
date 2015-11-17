package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.HasComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerMapSearchComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.MapSearchComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.MapSearchModule;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.fragment.MapSearchFragment;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;

public class MapSearchActivity extends NavigationDrawerActivity
        implements HasComponent<MapSearchComponent>, MapSearchFragment.MapSearchListener {

    MapSearchComponent mapSearchComponent;

    private GoogleMap mMap;

    private static final int DEFAULT_ZOOM_LEVEL = 15;

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
        setupInjector();
        setupMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapIfNeeded();
    }

    private void setupInjector() {
        mapSearchComponent = DaggerMapSearchComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mapSearchModule(new MapSearchModule())
                .build();
    }

    @Override
    public MapSearchComponent getComponent() {
        return mapSearchComponent;
    }

    private void setupMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                // Set map UI settings
                UiSettings mapUi = mMap.getUiSettings();
                mapUi.setMapToolbarEnabled(false);
                mapUi.setZoomControlsEnabled(true);
                mapUi.setMyLocationButtonEnabled(false);

                //TODO set map on info window click
                //TODO set map info window adapter
                mMap.setOnMapClickListener(onMapClickListener);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.layout_map_search_container, MapSearchFragment.newInstance())
                .commit();
            }
        }
    }

    @Override
    public void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    @Override
    public void renderStopList(Collection<StopModel> stopModels) {
        Log.d("MapSearchAct", "renderStopList: " + stopModels.size() + " stops"); //TODO temp
        if (mMap == null) {
            return;
        }

        for (StopModel stopModel : stopModels) {
            mMap.addMarker(new MarkerOptions()
                .title(stopModel.getName())
                .position(new LatLng(stopModel.getLat(), stopModel.getLon()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            //TODO snippet
            //TODO custom markers?
        }
    }

    @Override
    public void viewStop(StopModel stopModel) {
        //TODO handle inactive routes/stops correctly
        if (stopModel != null) {
            navigator.navigateToArrivalList(this, stopModel.getStopId());
        }
    }

    private MapSearchFragment getMapSearchFragment() {
        try {
            MapSearchFragment fragment = (MapSearchFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.layout_map_search_container);
            return fragment;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void getNearbyStops(double lat, double lon, String radiusInMiles) {
        MapSearchFragment fragment = getMapSearchFragment();
        if (fragment != null) {
            fragment.getNearbyStops(lat, lon, radiusInMiles);
        }
    }

    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(final LatLng latLng) {
            clearMap();
            //TODO show loading ui

            // Use Google's Geocoder API to search for the tapped location
            Geocoder geo = new Geocoder(getBaseContext(), Locale.US);
            String locationAddress = "Error loading address"; // set default value in case Geocoder can't find address
            try {
                List<Address> addresses = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);

                if (!addresses.isEmpty()) {
                    locationAddress = addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            //TODO update name of center marker

            mMap.addMarker(new MarkerOptions()
                    .title(locationAddress)
//                    .snippet(CENTER_MARKER) //TODO temp
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM_LEVEL),
                    new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {
                            getNearbyStops(
                                    latLng.latitude,
                                    latLng.longitude,
                                    MapSearchFragment.DEFAULT_RADIUS_MILES);
                        }

                        @Override
                        public void onCancel() {
                            getNearbyStops(
                                    latLng.latitude,
                                    latLng.longitude,
                                    MapSearchFragment.DEFAULT_RADIUS_MILES);
                        }
                    });
        }
    };
}
