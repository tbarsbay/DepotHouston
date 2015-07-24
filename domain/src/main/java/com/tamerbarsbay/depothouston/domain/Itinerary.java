package com.tamerbarsbay.depothouston.domain;

import java.util.Date;

/**
 * Created by Tamer on 7/24/2015.
 */
public class Itinerary {

    private final String itineraryId;
    private Date created;
    private Date startTime;
    private Date endTime;
    private Date adjustedStartTime;
    private Date adjustedEndTime;
    private String startStopName;
    private String endStopName;
    private String startStopId;
    private String endStopId;
    private String startAddress;
    private String endAddress;
    private String transferCount;
    private String travelTypes;
    private double walkDistance;
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;

    public Itinerary(String itineraryId) {
        this.itineraryId = itineraryId;
    }

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
