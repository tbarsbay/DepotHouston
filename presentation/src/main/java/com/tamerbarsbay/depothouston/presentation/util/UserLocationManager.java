package com.tamerbarsbay.depothouston.presentation.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class UserLocationManager implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private Location lastUserLocation;
    private UserLocationListener listener;

    public UserLocationManager(@NonNull Context context, @NonNull UserLocationListener listener) {
        this.listener = listener;
        googleApiClient = buildGoogleApiClient(context);
    }

    private GoogleApiClient buildGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    public void connect() {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void disconnect() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    public Location getUserLocation() {
        if (googleApiClient != null && googleApiClient.isConnected()) {
            return LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        } else {
            return null;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
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
