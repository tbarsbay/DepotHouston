package com.tamerbarsbay.depothouston.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Tamer on 7/22/2015.
 */
public class RouteEntity {

    @SerializedName("RouteId")
    private String routeId;

    @SerializedName("FinalStop0Id")
    private String finalStop0Id;

    @SerializedName("FinalStop1Id")
    private String finalStop1Id;

    @SerializedName("RouteName")
    private String routeName;

    @SerializedName("LongName")
    private String longName;

    @SerializedName("RouteType")
    private String routeType;

    public RouteEntity() {}

    public String getFinalStop0Id() {
        return finalStop0Id;
    }

    public void setFinalStop0Id(String finalStop0Id) {
        this.finalStop0Id = finalStop0Id;
    }

    public String getFinalStop1Id() {
        return finalStop1Id;
    }

    public void setFinalStop1Id(String finalStop1Id) {
        this.finalStop1Id = finalStop1Id;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
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

    public boolean isBus() {
        return this.getRouteType().equals("Bus");
    }

}
