package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public class DiskStopDataStore implements StopDataStore {

    private final StopCache stopCache;

    public DiskStopDataStore(StopCache stopCache) {
        this.stopCache = stopCache;
    }

    @Override
    public Observable<List<StopEntity>> getStopListByRoute(String routeId) {
        throw new UnsupportedOperationException("Operation not available.");
    }
}
