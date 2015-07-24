package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.ItineraryCache;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;

import rx.Observable;

/**
 * Created by Tamer on 7/24/2015.
 */
public class DiskItineraryDataStore implements ItineraryDataStore {

    private final ItineraryCache itineraryCache;

    public DiskItineraryDataStore(ItineraryCache itineraryCache) {
        this.itineraryCache = itineraryCache;
    }

    @Override
    public Observable<ItineraryEntity> calculateItinerary(double lat1, double lon1, double lat2, double lon2) {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    public Observable<ItineraryEntity> calculateItineraryWithEndTime(double lat1, double lon1, double lat2, double lon2, String endTime) {
        throw new UnsupportedOperationException("Operation not supported.");
    }

    @Override
    public Observable<ItineraryEntity> getItineraryDetails(String itineraryId) {
        //TODO
        return null;
    }
}
