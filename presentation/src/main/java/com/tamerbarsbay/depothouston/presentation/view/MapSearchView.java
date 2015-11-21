package com.tamerbarsbay.depothouston.presentation.view;

public interface MapSearchView extends StopListView {

    void clearMap();
    void plotCenterMarker(double lat, double lon);
    void showStopsView();
    void hideStopsView();

}
