package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.net.RestApi;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Tamer on 7/22/2015.
 */
public class CloudStopDataStore implements StopDataStore {

    private final RestApi restApi;
    private final StopCache stopCache;

    private final Action1<StopEntity> saveToCacheAction = new Action1<StopEntity>() {
        @Override
        public void call(StopEntity stopEntity) {
            if (stopEntity != null) {
                //StopCloudDataStore.this.stopCache.put(stopEntity); //TODO uncomment
            }
        }
    };

    public CloudStopDataStore(RestApi restApi, StopCache stopCache) {
        this.restApi = restApi;
        this.stopCache = stopCache;
    }

    @Override
    public Observable<List<StopEntity>> getStopListByRoute(String routeId) {
        return this.restApi.getStopsByRoute(routeId);
    }
}
