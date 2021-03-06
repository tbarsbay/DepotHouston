package com.tamerbarsbay.depothouston.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ItineraryEntity {

    @SerializedName("ItineraryId")
    private String itineraryId;

    @SerializedName("Created")
    private Date created;

    @SerializedName("StartTime")
    private Date startTime;

    @SerializedName("EndTime")
    private Date endTime;

    @SerializedName("AdjustedStartTime")
    private Date adjustedStartTime;

    @SerializedName("AdjustedEndTime")
    private Date adjustedEndTime;

    @SerializedName("StartStopName")
    private String startStopName;

    @SerializedName("EndStopName")
    private String endStopName;

    @SerializedName("StartStopId")
    private String startStopId;

    @SerializedName("EndStopId")
    private String endStopId;

    @SerializedName("StartAddress")
    private String startAddress;

    @SerializedName("EndAddress")
    private String endAddress;

    @SerializedName("TransferCount")
    private String transferCount;

    @SerializedName("TravelTypes")
    private String travelTypes;

    @SerializedName("WalkDistance")
    private double walkDistance;

    @SerializedName("StartLat")
    private double startLat;

    @SerializedName("StartLon")
    private double startLon;

    @SerializedName("EndLat")
    private double endLat;

    @SerializedName("EndLon")
    private double endLon;

    public ItineraryEntity() {}

    public Date getAdjustedEndTime() {
        return adjustedEndTime;
    }

    public void setAdjustedEndTime(Date adjustedEndTime) {
        this.adjustedEndTime = adjustedEndTime;
    }

    public Date getAdjustedStartTime() {
        return adjustedStartTime;
    }

    public void setAdjustedStartTime(Date adjustedStartTime) {
        this.adjustedStartTime = adjustedStartTime;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public String getEndStopId() {
        return endStopId;
    }

    public void setEndStopId(String endStopId) {
        this.endStopId = endStopId;
    }

    public String getEndStopName() {
        return endStopName;
    }

    public void setEndStopName(String endStopName) {
        this.endStopName = endStopName;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(String itineraryId) {
        this.itineraryId = itineraryId;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public String getStartStopId() {
        return startStopId;
    }

    public void setStartStopId(String startStopId) {
        this.startStopId = startStopId;
    }

    public String getStartStopName() {
        return startStopName;
    }

    public void setStartStopName(String startStopName) {
        this.startStopName = startStopName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getTransferCount() {
        return transferCount;
    }

    public void setTransferCount(String transferCount) {
        this.transferCount = transferCount;
    }

    public String getTravelTypes() {
        return travelTypes;
    }

    public void setTravelTypes(String travelTypes) {
        this.travelTypes = travelTypes;
    }

    public double getWalkDistance() {
        return walkDistance;
    }

    public void setWalkDistance(double walkDistance) {
        this.walkDistance = walkDistance;
    }
}
