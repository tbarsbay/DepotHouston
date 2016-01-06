package com.tamerbarsbay.depothouston.presentation.model;

import android.support.annotation.IntRange;

public class WidgetModel {

    private int widgetId;
    private String routeId;
    private String direction;
    private String stopId;
    private String title;
    private int size;
    private int bgColor;

    public WidgetModel(int widgetId, String routeId, String direction, String stopId,
                       String title, @IntRange(from=0, to=1) int size,
                       @IntRange(from=0, to=8) int bgColor) {
        this.widgetId = widgetId;
        this.routeId = routeId;
        this.direction = direction;
        this.stopId = stopId;
        this.title = title;
        this.size = size;
        this.bgColor = bgColor;
    }

    public int getWidgetId() {
        return widgetId;
    }

    public int getBgColor() {
        return bgColor;
    }

    public int getSize() {
        return size;
    }

    public String getDirection() {
        return direction;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getStopId() {
        return stopId;
    }

    public String getTitle() {
        return title;
    }
}
