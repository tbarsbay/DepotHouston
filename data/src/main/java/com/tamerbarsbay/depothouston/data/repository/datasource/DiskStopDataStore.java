package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.util.List;

import rx.Observable;

public class DiskStopDataStore implements StopDataStore {

    private final StopCache stopCache;

    public DiskStopDataStore(StopCache stopCache) {
        this.stopCache = stopCache;
    }

    @Override
    public Observable<List<StopEntity>> stopsByRoute(String routeId) {
        throw new UnsupportedOperationException("Operation not available.");
    }

    @Override
    public Observable<List<StopEntity>> stopsNearLocation(double lat, double lon, String radiusInMiles) {
        //TODO
        return null;
    }
}
