package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.ItineraryModel;

/**
 * Created by Tamer on 7/26/2015.
 */
public interface ItineraryDetailsView extends LoadDataView {

    void renderItinerary(ItineraryModel itineraryModel);

}
