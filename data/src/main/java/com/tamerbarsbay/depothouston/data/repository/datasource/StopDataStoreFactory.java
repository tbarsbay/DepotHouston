package com.tamerbarsbay.depothouston.data.repository.datasource;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.net.HoustonMetroApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StopDataStoreFactory {

    private Context context;
    private final StopCache stopCache;

    @Inject
    public StopDataStoreFactory(Context context, StopCache stopCache) {
        if (context == null || stopCache == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context;
        this.stopCache = stopCache;
    }

    public StopDataStore create(String stopId) {
        //TODO
        return null;
    }

    public StopDataStore createCloudDataStore() {
        HoustonMetroApi houstonMetroApi = new HoustonMetroApi(this.context);
        return new CloudStopDataStore(houstonMetroApi, this.stopCache);
    }

}
