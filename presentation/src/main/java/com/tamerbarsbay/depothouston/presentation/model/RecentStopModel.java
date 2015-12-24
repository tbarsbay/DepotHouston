package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

public class RecentStopModel {

    private String stopId;
    private String name;

    public RecentStopModel(@NonNull String stopId, @NonNull String name) {
        this.stopId = stopId;
        this.name = name;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(@NonNull String stopId) {
        this.stopId = stopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
