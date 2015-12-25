package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class UserLocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private Location lastUserLocation;
    private UserLocationListener listener;
    private Context context;

    public UserLocationManager(@NonNull Context context, @NonNull UserLocationListener listener) {
        this.listener = listener;
        this.context = context;
        googleApiClient = buildGoogleApiClient(context);
    }

    public void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private GoogleApiClient buildGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void disconnect() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public Location getUserLocation() {
        return lastUserLocation != null ? lastUserLocation : getUserLocationFromLocationManager();
    }

    private Location getUserLocationFromLocationManager() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (provider == null) {
            // There was an error, or the user did not give Depot location permission in Android M
            return null;
        }

        Location userLocation = locationManager.getLastKnownLocation(provider);
        // If the best provider given by the LocationManager did not work
        // for some reason, try the cell network as the provider.
        if (userLocation == null) {
            provider = "network";
            userLocation = locationManager.getLastKnownLocation(provider);
        }
        // If the cell network did not give a valid location for the user,
        // try the battery-saving passive provider (uses nearby wifi).
        if (userLocation == null) {
            provider = "passive";
            userLocation = locationManager.getLastKnownLocation(provider);
        }
        return userLocation;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (googleApiClient != null) {
            lastUserLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        }
        if (listener != null) {
            listener.onConnected();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (listener != null) {
            listener.onConnectionFailed();
        }
    }

    public interface UserLocationListener {
        /**
         * Called when the Google API client successfully connects.
         */
        void onConnected();

        /**
         * Called when the Google API client fails to connect.
         */
        void onConnectionFailed();
    }
}
