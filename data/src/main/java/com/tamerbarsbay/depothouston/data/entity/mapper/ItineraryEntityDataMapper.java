package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.domain.Itinerary;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/24/2015.
 */
@Singleton
public class ItineraryEntityDataMapper {

    @Inject
    public ItineraryEntityDataMapper() {}

    public Itinerary transform(ItineraryEntity itineraryEntity) {
        Itinerary itinerary = null;
        if (itineraryEntity != null) {
            itinerary = new Itinerary(itineraryEntity.getItineraryId());
            itinerary.setAdjustedEndTime(itineraryEntity.getAdjustedEndTime());
            itinerary.setAdjustedStartTime(itineraryEntity.getAdjustedStartTime());
            itinerary.setCreated(itineraryEntity.getCreated());
            itinerary.setEndAddress(itineraryEntity.getEndAddress());
            itinerary.setEndLat(itineraryEntity.getEndLat());
            itinerary.setEndLon(itineraryEntity.getEndLon());
            itinerary.setEndStopId(itineraryEntity.getEndStopId());
            itinerary.setEndStopName(itineraryEntity.getEndStopName());
            itinerary.setEndTime(itineraryEntity.getEndTime());
            itinerary.setStartAddress(itineraryEntity.getStartAddress());
            itinerary.setStartLat(itineraryEntity.getStartLat());
            itinerary.setStartLon(itineraryEntity.getStartLon());
            itinerary.setStartStopId(itineraryEntity.getStartStopId());
            itinerary.setStartStopName(itineraryEntity.getStartStopName());
            itinerary.setStartTime(itineraryEntity.getStartTime());
            itinerary.setTransferCount(itineraryEntity.getTransferCount());
            itinerary.setTravelTypes(itineraryEntity.getTravelTypes());
            itinerary.setWalkDistance(itineraryEntity.getWalkDistance());
        }
        return itinerary;
    }

    public List<Itinerary> transform(List<ItineraryEntity> itineraryEntities) {
        List<Itinerary> itineraries = new ArrayList<Itinerary>();
        Itinerary itinerary = null;
        for (ItineraryEntity itineraryEntity : itineraryEntities) {
            itinerary = transform(itineraryEntity);
            if (itinerary != null) {
                itineraries.add(itinerary);
            }
        }
        return itineraries;
    }

}
