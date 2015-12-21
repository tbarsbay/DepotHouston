package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Represents a group of user-saved transit stops.
 */
public class SavedGroupModel {

    private final long id;
    private String name;
    private ArrayList<SavedStopModel> stops = new ArrayList<SavedStopModel>();
    private long nextChildId;

    public SavedGroupModel(long id, @NonNull String name, @NonNull ArrayList<SavedStopModel> stops) {
        this.id = id;
        this.name = name;
        this.stops = stops;
        this.nextChildId = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public ArrayList<SavedStopModel> getStops() {
        return stops;
    }

    public int getNumStops() {
        return getStops() != null ? getStops().size() : 0;
    }

    public void setStops(@NonNull ArrayList<SavedStopModel> stops) {
        this.stops = stops;
    }

    public void addStop(@NonNull SavedStopModel stop) {
        if (stops != null) {
            stops.add(stop);
        }
    }

    public long generateNewChildId() {
        final long id = nextChildId;
        nextChildId += 1;
        return id;
    }
}
