package com.tamerbarsbay.depothouston.domain;

import java.util.Date;

/**
 * Created by Tamer on 7/23/2015.
 */
public class Incident {

    private final String incidentId;
    private String status;
    private Date affectedFrom;
    private Date affectedTo;
    private String severity;
    private String description;
    private String emergencyText;
    private String duration;

    public Incident(String incidentId) {
        this.incidentId = incidentId;
    }

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

    public String getIncidentId() {
        return incidentId;
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
}
