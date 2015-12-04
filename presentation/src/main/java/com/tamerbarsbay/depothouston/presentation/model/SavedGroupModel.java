package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Represents a group of user-saved transit stops.
 */
public class SavedGroupModel {

    private String name;
    private int rank;
    private ArrayList<SavedStopModel> stops;

    public SavedGroupModel(@NonNull String name, int rank, @NonNull ArrayList<SavedStopModel> stops) {
        this.name = name;
        this.rank = rank;
        this.stops = stops;
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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
}
