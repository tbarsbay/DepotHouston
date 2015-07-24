package com.tamerbarsbay.depothouston.data.cache;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface RouteCache {

    Observable<RouteEntity> get(final String routeId);
    void put(RouteEntity routeEntity);

}
