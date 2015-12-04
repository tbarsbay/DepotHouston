package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.NonNull;

public class SavedStopModel extends StopModel {

    private int rank;

    public SavedStopModel(@NonNull String id, @NonNull String name) {
        super(id); //TODO why is this here?
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
