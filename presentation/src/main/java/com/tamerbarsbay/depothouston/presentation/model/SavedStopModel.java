package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

public class SavedStopModel {

    private long objectId;
    private String stopId;
    private String name;

    public SavedStopModel(long objectId, @NonNull String stopId, @NonNull String name) {
        this.objectId = objectId;
        this.stopId = stopId;
        this.name = name;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long id) {
        this.objectId = id;
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
