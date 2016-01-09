package com.tamerbarsbay.depothouston.presentation.model;

public class ChangelogItemModel {

    private String version;
    private String date;
    private String changes;

    public ChangelogItemModel(String version, String date, String changes) {
        this.version = version;
        this.date = date;
        this.changes = changes;
    }

    public String getChanges() {
        return changes;
    }

    public String getDate() {
        return date;
    }

    public String getVersion() {
        return "v" + version;
    }
}
