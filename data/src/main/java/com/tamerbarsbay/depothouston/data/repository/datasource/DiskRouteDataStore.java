package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public class DiskRouteDataStore implements RouteDataStore {

    private final RouteCache routeCache;

    public DiskRouteDataStore(RouteCache routeCache) {
        this.routeCache = routeCache;
    }

    @Override
    public Observable<RouteEntity> getRouteDetails(String routeId) {
        //TODO
        return null;
    }

    @Override
    public Observable<List<RouteEntity>> getRouteList() {
        throw new UnsupportedOperationException("Operation not available.");
    }
}
