package com.tamerbarsbay.depothouston.presentation.model;

public class WidgetModel {

    private String routeId;
    private String direction;
    private String stopId;
    private String title;
    private int bgColor;

    public WidgetModel(String routeId, String direction, String stopId, String title, int bgColor) {
        this.routeId = routeId;
        this.direction = direction;
        this.stopId = stopId;
        this.title = title;
        this.bgColor = bgColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
