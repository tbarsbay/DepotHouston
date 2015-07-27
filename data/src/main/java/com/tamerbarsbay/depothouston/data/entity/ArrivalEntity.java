package com.tamerbarsbay.depothouston.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ArrivalEntity {

    @SerializedName("ArrivalId")
    private String arrivalId;

    @SerializedName("LocalArrivalTime")
    private String localArrivalTimeString;

    @SerializedName("LocalDepartureTime")
    private String localDepartureTimeString;

    @SerializedName("UtcArrivalTime")
    private String utcArrivalTimeString;

    @SerializedName("UtcDepartureTime")
    private String utcDepartureTimeString;

    @SerializedName("DestinationName")
    private String destinationName;

    @SerializedName("DestinationStopId")
    private String destinationStopId;

    @SerializedName("RouteId")
    private String routeId;

    @SerializedName("StopId")
    private String stopId;

    @SerializedName("StopName")
    private String stopName;

    @SerializedName("StopSequence")
    private String stopSequence;

    @SerializedName("RouteName")
    private String routeName;

    @SerializedName("RouteType")
    private String routeType;

    @SerializedName("DelaySeconds")
    private int delaySeconds;

    @SerializedName("IsRealTime")
    private boolean isRealTime;

    private Date localArrivalTime;
    private Date localDepartureTime;
    private Date utcArrivalTime;
    private Date utcDepartureTime;

    public ArrivalEntity() {}

    public String getArrivalId() {
        return arrivalId;
    }

    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDestinationStopId() {
        return destinationStopId;
    }

    public void setDestinationStopId(String destinationStopId) {
        this.destinationStopId = destinationStopId;
    }

    public boolean isRealTime() {
        return isRealTime;
    }

    public void setIsRealTime(boolean isRealTime) {
        this.isRealTime = isRealTime;
    }

    public Date getLocalArrivalTime() {
        return localArrivalTime == null
                ? parseStringAsDate(localArrivalTimeString)
                : localArrivalTime;
    }

    public void setLocalArrivalTime(Date localArrivalTime) {
        this.localArrivalTime = localArrivalTime;
    }

    public Date getLocalDepartureTime() {
        return localDepartureTime == null
                ? parseStringAsDate(localDepartureTimeString)
                : localDepartureTime;
    }

    public void setLocalDepartureTime(Date localDepartureTime) {
        this.localDepartureTime = localDepartureTime;
    }

    public Date getUtcArrivalTime() {
        return utcArrivalTime == null
                ? parseStringAsDate(utcArrivalTimeString)
                : utcArrivalTime;
    }

    public void setUtcArrivalTime(Date utcArrivalTime) {
        this.utcArrivalTime = utcArrivalTime;
    }

    public Date getUtcDepartureTime() {
        return utcDepartureTime == null
                ? parseStringAsDate(utcDepartureTimeString)
                : utcDepartureTime;
    }

    public void setUtcDepartureTime(Date utcDepartureTime) {
        this.utcDepartureTime = utcDepartureTime;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    public Date parseStringAsDate(String s) {
        // Things in the json are formatted like this: "\/Date(0000000000000)/".
        // Where all the 0s are some date in milliseconds. We have to parse
        // those numbers as a long and construct a Date object from them.
        try {
            String millisString = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));
            return new Date(Long.parseLong(millisString));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}
