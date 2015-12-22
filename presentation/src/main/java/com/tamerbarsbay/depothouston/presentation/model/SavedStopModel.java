package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

public class SavedStopModel {

    private int objectId;
    private String stopId;
    private String name;

    public SavedStopModel(int objectId, @NonNull String stopId, @NonNull String name) {
        this.objectId = objectId;
        this.stopId = stopId;
        this.name = name;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int id) {
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
