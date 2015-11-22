package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;

import rx.Observable;

public interface ItineraryDataStore {

    Observable<ItineraryEntity> calculateItinerary(final double lat1, final double lon1,
                                                   final double lat2, final double lon2);

    Observable<ItineraryEntity> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                              final double lat2, final double lon2,
                                                              final String endTime);

    Observable<ItineraryEntity> itinerary(final String itineraryId);

}
