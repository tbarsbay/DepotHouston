package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.ItineraryCache;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.net.HoustonMetroApi;

import rx.Observable;
import rx.functions.Action1;

public class CloudItineraryDataStore implements ItineraryDataStore {

    private final HoustonMetroApi houstonMetroApi;
    private final ItineraryCache itineraryCache;

    private final Action1<ItineraryEntity> saveToCacheAction = new Action1<ItineraryEntity>() {
        @Override
        public void call(ItineraryEntity itineraryEntity) {
        if (itineraryEntity != null) {
            CloudItineraryDataStore.this.itineraryCache.put(itineraryEntity);
        }
        }
    };

    public CloudItineraryDataStore(HoustonMetroApi houstonMetroApi, ItineraryCache itineraryCache) {
        this.houstonMetroApi = houstonMetroApi;
        this.itineraryCache = itineraryCache;
    }

    @Override
    public Observable<ItineraryEntity> calculateItinerary(double lat1, double lon1, double lat2, double lon2) {
        return this.houstonMetroApi.calculateItinerary(lat1, lon1, lat2, lon2).doOnNext(saveToCacheAction);
    }

    @Override
    public Observable<ItineraryEntity> calculateItineraryWithEndTime(double lat1, double lon1,
                                                                     double lat2, double lon2,
                                                                     String endTime) {
        return this.houstonMetroApi.calculateItineraryWithEndTime(lat1, lon1, lat2, lon2, endTime)
                .doOnNext(saveToCacheAction);
    }

    @Override
    public Observable<ItineraryEntity> itinerary(String itineraryId) {
        return this.houstonMetroApi.itinerary(itineraryId).doOnNext(saveToCacheAction);
    }
}
