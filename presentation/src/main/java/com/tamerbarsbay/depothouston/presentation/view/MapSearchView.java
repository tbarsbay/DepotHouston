package com.tamerbarsbay.depothouston.presentation.view;

public interface MapSearchView extends StopListView {

    // Zoom levels for the Google Map
    int ZOOM_LEVEL_CLOSE = 15;
    int ZOOM_LEVEL_FAR = 12;

    void clearMap();
    void centerMapOn(double lat, double lon, int zoomLevel);
    void plotCenterMarker(String centerAddress, double lat, double lon);
    void showStopsView();
    void hideStopsView();
    void collapseMapView();
    void expandMapView();

}
