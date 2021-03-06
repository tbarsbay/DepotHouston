package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.net.HoustonMetroApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

public class CloudStopDataStore implements StopDataStore {

    private final HoustonMetroApi houstonMetroApi;
    private final StopCache stopCache;

    private final Action1<StopEntity> saveToCacheAction = new Action1<StopEntity>() {
        @Override
        public void call(StopEntity stopEntity) {
            if (stopEntity != null) {
                CloudStopDataStore.this.stopCache.put(stopEntity);
            }
        }
    };

    public CloudStopDataStore(HoustonMetroApi houstonMetroApi, StopCache stopCache) {
        this.houstonMetroApi = houstonMetroApi;
        this.stopCache = stopCache;
    }

    @Override
    public Observable<List<StopEntity>> stopsByRoute(String routeId, String direction) {
        return houstonMetroApi.stopsByRoute(routeId, direction);
    }

    @Override
    public Observable<List<StopEntity>> stopsNearLocation(double lat, double lon, String radiusInMiles) {
        return houstonMetroApi.stopsNearLocation(lat, lon, radiusInMiles);
    }

    @Override
    public Observable<List<StopEntity>> stopsNearLocationByRoute(String routeId, double lat, double lon, String radiusInMiles) {
        return houstonMetroApi.stopsNearLocationByRoute(routeId, lat, lon, radiusInMiles);
    }
}
