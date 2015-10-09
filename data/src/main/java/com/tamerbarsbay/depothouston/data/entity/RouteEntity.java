package com.tamerbarsbay.depothouston.data.entity;

import com.google.gson.annotations.SerializedName;

public class RouteEntity {

    @SerializedName("RouteId")
    private String routeId;

    @SerializedName("AgencyAbbreviation")
    private String agencyAbbreviation;

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

    @SerializedName("DataSource")
    private String dataSource;

    @SerializedName("FinalStop0")
    private FinalStop0 finalStop0;

    @SerializedName("FinalStop1")
    private FinalStop1 finalStop1;

    @SerializedName("Directions")
    private Directions directions;

    @SerializedName("NonOrderedStops")
    private NonOrderedStops nonOrderedStops;

    @SerializedName("Stops")
    private Stops stops;

    @SerializedName("Incidents")
    private Incidents incidents;

    @SerializedName("Arrivals")
    private Arrivals arrivals;

    class Deferred {
        @SerializedName("uri")
        private String url;
    }

    class FinalStop0 {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class FinalStop1 {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class Directions {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class NonOrderedStops {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class Stops {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class Incidents {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    class Arrivals {
        @SerializedName("__deferred")
        private Deferred deferred;
    }

    //TODO add links to other things like directions, arrivals, etc

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
