package com.tamerbarsbay.depothouston.presentation.model;

import org.apache.commons.lang3.text.WordUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ArrivalModel {
    private String arrivalId;
    private Date localArrivalTime;
    private Date localDepartureTime;
    private Date utcArrivalTime;
    private Date utcDepartureTime;
    private String destinationName;
    private String destinationStopId;
    private String routeId;
    private String stopId;
    private String stopName;
    private String stopSequence;
    private String routeName;
    private String routeType;
    private int delaySeconds;
    private boolean isRealTime;

    public ArrivalModel(String arrivalId) {
        this.arrivalId = arrivalId;
    }

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
        return WordUtils.capitalizeFully(destinationName);
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
        return localArrivalTime;
    }

    public void setLocalArrivalTime(Date localArrivalTime) {
        this.localArrivalTime = localArrivalTime;
    }

    public Date getLocalDepartureTime() {
        return localDepartureTime;
    }

    public void setLocalDepartureTime(Date localDepartureTime) {
        this.localDepartureTime = localDepartureTime;
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

    public Date getUtcArrivalTime() {
        return utcArrivalTime;
    }

    public void setUtcArrivalTime(Date utcArrivalTime) {
        this.utcArrivalTime = utcArrivalTime;
    }

    public Date getUtcDepartureTime() {
        return utcDepartureTime;
    }

    public void setUtcDepartureTime(Date utcDepartureTime) {
        this.utcDepartureTime = utcDepartureTime;
    }

    public long getMinsUntilArrival() {
        long diff = getUtcArrivalTime().getTime() - System.currentTimeMillis();
        long diffInMins = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
        return diffInMins;
    }
}
