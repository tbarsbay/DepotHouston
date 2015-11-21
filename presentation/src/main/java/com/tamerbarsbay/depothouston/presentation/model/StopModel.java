package com.tamerbarsbay.depothouston.presentation.model;

import org.apache.commons.lang3.text.WordUtils;

public class StopModel {

    private final String id;
    private String stopId;
    private String directionId;
    private String routeId;
    private String agencyId;
    private String name;
    private String stopCode;
    private String type;
    private double lat;
    private double lon;
    private String ordinal;
    private String ordinalOnDirection;

    public StopModel(String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    public String getId() {
        return id;
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

    public String getName() {
        return WordUtils.capitalizeFully(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(String ordinal) {
        this.ordinal = ordinal;
    }

    public String getOrdinalOnDirection() {
        return ordinalOnDirection;
    }

    public void setOrdinalOnDirection(String ordinalOnDirection) {
        this.ordinalOnDirection = ordinalOnDirection;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopCode() {
        return stopCode;
    }

    public void setStopCode(String stopCode) {
        this.stopCode = stopCode;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBus() {
        return this.getType().equals("Bus");
    }

//    public class StopComparator extends Comparator<StopModel> {
//        public int compare(StopModel stop1, StopModel stop2) {
//            return Double.compare
//        }
//    }
}
