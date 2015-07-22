package com.tamerbarsbay.depothouston.domain;

/**
 * Created by Tamer on 7/22/2015.
 */
public class Route {

    private final String routeId;
    private String finalStop0Id;
    private String finalStop1Id;
    private String routeName;
    private String longName;
    private String routeType;

    public Route(String routeId) {
        this.routeId = routeId;
    }

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
