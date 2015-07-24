package com.tamerbarsbay.depothouston.data.cache;

import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;

import rx.Observable;

/**
 * Created by Tamer on 7/24/2015.
 */
public interface ItineraryCache {

    Observable<ItineraryEntity> get(final String itineraryId);
    void put(ItineraryEntity itineraryEntity);

}
