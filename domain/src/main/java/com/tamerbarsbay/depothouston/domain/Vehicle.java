package com.tamerbarsbay.depothouston.domain;

/**
 * Created by Tamer on 7/23/2015.
 */
public class Vehicle {
    private String vehicleId;
    private String routeId;
    private String routeName;
    private String directionName;
    private String destinationName;
    private int delaySeconds;
    private String vehicleReportTime;
    private boolean isMonitored;
    private double lat;
    private double lon;
    private String block;

    public Vehicle(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
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

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public boolean isMonitored() {
        return isMonitored;
    }

    public void setIsMonitored(boolean isMonitored) {
        this.isMonitored = isMonitored;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
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

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleReportTime() {
        return vehicleReportTime;
    }

    public void setVehicleReportTime(String vehicleReportTime) {
        this.vehicleReportTime = vehicleReportTime;
    }
}
