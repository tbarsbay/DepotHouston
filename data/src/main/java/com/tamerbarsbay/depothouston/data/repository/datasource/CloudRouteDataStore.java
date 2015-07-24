package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.net.RestApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Tamer on 7/22/2015.
 */
public class CloudRouteDataStore implements RouteDataStore {

    private final RestApi restApi;
    private final RouteCache routeCache;

    private final Action1<RouteEntity> saveToCacheAction = new Action1<RouteEntity>() {
        @Override
        public void call(RouteEntity routeEntity) {
            if (routeEntity != null) {
                //CloudRouteDataStore.this.routeCache.put(routeEntity); //TODO uncomment
            }
        }
    };

    public CloudRouteDataStore(RestApi restApi, RouteCache routeCache) {
        this.restApi = restApi;
        this.routeCache = routeCache;
    }

    @Override
    public Observable<List<RouteEntity>> getRouteList() {
        //TODO also put in cache?
        return this.restApi.getRouteList();
    }

    @Override
    public Observable<RouteEntity> getRouteDetails(String routeId) {
        return this.restApi.getRouteDetails(routeId).doOnNext(saveToCacheAction);
    }
}
