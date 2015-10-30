package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.ItineraryModel;

public interface ItineraryDetailsView extends LoadDataView {

    void renderItinerary(ItineraryModel itineraryModel);

}
