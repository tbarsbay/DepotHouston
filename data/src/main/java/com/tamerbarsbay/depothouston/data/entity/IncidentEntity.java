package com.tamerbarsbay.depothouston.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Tamer on 7/23/2015.
 */
public class IncidentEntity {

    @SerializedName("Id")
    private String incidentId;

    @SerializedName("Status")
    private String status;

    @SerializedName("AffectedFrom")
    private Date affectedFrom;

    @SerializedName("AffectedTo")
    private Date affectedTo;

    @SerializedName("Severity")
    private String severity;

    @SerializedName("Description")
    private String description;

    @SerializedName("EmergencyText")
    private String emergencyText;

    @SerializedName("Duration")
    private String duration;

    public IncidentEntity() {}

    public Date getAffectedFrom() {
        return affectedFrom;
    }

    public void setAffectedFrom(Date affectedFrom) {
        this.affectedFrom = affectedFrom;
    }

    public Date getAffectedTo() {
        return affectedTo;
    }

    public void setAffectedTo(Date affectedTo) {
        this.affectedTo = affectedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEmergencyText() {
        return emergencyText;
    }

    public void setEmergencyText(String emergencyText) {
        this.emergencyText = emergencyText;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public void setIncidentId(String incidentId) {
        this.incidentId = incidentId;
    }
}
