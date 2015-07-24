package com.tamerbarsbay.depothouston.data.cache;

import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface StopCache {

    Observable<StopEntity> get(final String stopId);
    void put(StopEntity stopEntity);

}
