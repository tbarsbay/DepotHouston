package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Itinerary;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.ItineraryModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/24/2015.
 */
@PerActivity
public class ItineraryModelDataMapper {

    @Inject
    public ItineraryModelDataMapper() {}

    public ItineraryModel transform(Itinerary itinerary) {
        if (itinerary == null) {
            throw new IllegalArgumentException("Cannot transform a null itinerary.");
        }
        ItineraryModel itineraryModel = new ItineraryModel(itinerary.getItineraryId());
        itineraryModel.setAdjustedEndTime(itinerary.getAdjustedEndTime());
        itineraryModel.setAdjustedStartTime(itinerary.getAdjustedStartTime());
        itineraryModel.setCreated(itinerary.getCreated());
        itineraryModel.setEndAddress(itinerary.getEndAddress());
        itineraryModel.setEndLat(itinerary.getEndLat());
        itineraryModel.setEndLon(itinerary.getEndLon());
        itineraryModel.setEndStopId(itinerary.getEndStopId());
        itineraryModel.setEndStopName(itinerary.getEndStopName());
        itineraryModel.setEndTime(itinerary.getEndTime());
        itineraryModel.setStartAddress(itinerary.getStartAddress());
        itineraryModel.setStartLat(itinerary.getStartLat());
        itineraryModel.setStartLon(itinerary.getStartLon());
        itineraryModel.setStartStopId(itinerary.getStartStopId());
        itineraryModel.setStartStopName(itinerary.getStartStopName());
        itineraryModel.setStartTime(itinerary.getStartTime());
        itineraryModel.setTransferCount(itinerary.getTransferCount());
        itineraryModel.setTravelTypes(itinerary.getTravelTypes());
        itineraryModel.setWalkDistance(itinerary.getWalkDistance());
        return itineraryModel;
    }

    public Collection<ItineraryModel> transform(Collection<Itinerary> itineraries) {
        Collection<ItineraryModel> itineraryModels;

        if (itineraries != null && !itineraries.isEmpty()) {
            itineraryModels = new ArrayList<ItineraryModel>();
            for (Itinerary itinerary : itineraries) {
                itineraryModels.add(transform(itinerary));
            }
        } else {
            itineraryModels = Collections.emptyList();
        }

        return itineraryModels;
    }
}
