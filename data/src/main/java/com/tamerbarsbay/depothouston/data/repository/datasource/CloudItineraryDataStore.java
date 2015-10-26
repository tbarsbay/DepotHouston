package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.ItineraryCache;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.net.RestApi;

import rx.Observable;
import rx.functions.Action1;

public class CloudItineraryDataStore implements ItineraryDataStore {

    private final RestApi restApi;
    private final ItineraryCache itineraryCache;

    private final Action1<ItineraryEntity> saveToCacheAction = new Action1<ItineraryEntity>() {
        @Override
        public void call(ItineraryEntity itineraryEntity) {
        if (itineraryEntity != null) {
            CloudItineraryDataStore.this.itineraryCache.put(itineraryEntity);
        }
        }
    };

    public CloudItineraryDataStore(RestApi restApi, ItineraryCache itineraryCache) {
        this.restApi = restApi;
        this.itineraryCache = itineraryCache;
    }

    @Override
    public Observable<ItineraryEntity> calculateItinerary(double lat1, double lon1, double lat2, double lon2) {
        return this.restApi.calculateItinerary(lat1, lon1, lat2, lon2).doOnNext(saveToCacheAction);
    }

    @Override
    public Observable<ItineraryEntity> calculateItineraryWithEndTime(double lat1, double lon1,
                                                                     double lat2, double lon2,
                                                                     String endTime) {
        return this.restApi.calculateItineraryWithEndTime(lat1, lon1, lat2, lon2, endTime)
                .doOnNext(saveToCacheAction);
    }

    @Override
    public Observable<ItineraryEntity> itinerary(String itineraryId) {
        return this.restApi.itinerary(itineraryId).doOnNext(saveToCacheAction);
    }
}
