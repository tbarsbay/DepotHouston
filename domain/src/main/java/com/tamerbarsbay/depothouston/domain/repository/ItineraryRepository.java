package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Itinerary;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Itinerary} related data.
 * Created by Tamer on 7/24/2015.
 */
public interface ItineraryRepository {

    Observable<Itinerary> calculateItinerary(final double lat1, final double lon1,
                                             final double lat2, final double lon2);

    Observable<Itinerary> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                        final double lat2, final double lon2,
                                                        final String endTime);

    Observable<Itinerary> itinerary(final String itineraryId);
}
