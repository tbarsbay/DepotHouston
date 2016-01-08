package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.net.HoustonMetroApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class CloudRouteDataStore implements RouteDataStore {

    private final HoustonMetroApi houstonMetroApi;
    private final RouteCache routeCache;

    private final Action1<RouteEntity> saveToCacheAction = new Action1<RouteEntity>() {
        @Override
        public void call(RouteEntity routeEntity) {
        if (routeEntity != null) {
            CloudRouteDataStore.this.routeCache.put(routeEntity);
        }
        }
    };

    public CloudRouteDataStore(HoustonMetroApi houstonMetroApi, RouteCache routeCache) {
        this.houstonMetroApi = houstonMetroApi;
        this.routeCache = routeCache;
    }

    @Override
    public Observable<List<RouteEntity>> routes() {
        //TODO also put in cache?
        return houstonMetroApi.routes();
    }

    @Override
    public Observable<RouteEntity> route(String routeId) {
        return houstonMetroApi.route(routeId).doOnNext(saveToCacheAction);
    }

    @Override
    public Observable<List<RouteEntity>> routesNearLocation(double lat, double lon, String radiusInMiles) {
        return houstonMetroApi.routesNearLocation(lat, lon, radiusInMiles);
    }

    @Override
    public Observable<List<RouteEntity>> routesByStop(String stopId) {
        return houstonMetroApi.routesByStop(stopId);
    }
}
