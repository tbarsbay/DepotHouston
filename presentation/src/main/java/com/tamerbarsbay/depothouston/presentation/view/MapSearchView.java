package com.tamerbarsbay.depothouston.presentation.view;

public interface MapSearchView extends StopListView {

    void clearMap();
    void centerMapOn(double lat, double lon);
    void plotCenterMarker(String centerAddress, double lat, double lon);
    void showStopsView();
    void hideStopsView();
    void collapseMapView();
    void expandMapView();

}
