package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import java.util.List;

import rx.Observable;

public class DiskRouteDataStore implements RouteDataStore {

    private final RouteCache routeCache;

    public DiskRouteDataStore(RouteCache routeCache) {
        this.routeCache = routeCache;
    }

    @Override
    public Observable<RouteEntity> route(String routeId) {
        //TODO
        return null;
    }

    @Override
    public Observable<List<RouteEntity>> routes() {
        throw new UnsupportedOperationException("Operation not available.");
    }

    @Override
    public Observable<List<RouteEntity>> routesNearLocation(double lat, double lon, String radiusInMiles) {
        throw new UnsupportedOperationException("Operation not available.");
    }

    @Override
    public Observable<List<RouteEntity>> routesByStop(String stopId) {
        throw new UnsupportedOperationException("Operation not available.");
    }
}
