package com.tamerbarsbay.depothouston.presentation.model;

import org.apache.commons.lang3.text.WordUtils;

public class RouteModel {

    private final String routeId;
    private String finalStop0Id;
    private String finalStop1Id;
    private String routeName;
    private String longName;
    private String routeType;

    public RouteModel(String routeId) {
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
        return WordUtils.capitalizeFully(longName);
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getRouteName() {
        return trimLeadingZeros(routeName);
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

    private String trimLeadingZeros(String s) {
        while (s.length() > 1 && s.charAt(0) == '0') {
            s = s.substring(1);
        }
        return s;
    }
}
