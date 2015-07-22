package com.tamerbarsbay.depothouston.data.repository.datasource;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.entity.mapper.StopEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.net.RestApi;
import com.tamerbarsbay.depothouston.data.net.RestApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/22/2015.
 */
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
        StopEntityJsonMapper stopEntityJsonMapper = new StopEntityJsonMapper();
        RestApi restApi = new RestApiImpl(this.context, stopEntityJsonMapper);
        return new CloudStopDataStore(restApi, this.stopCache);
    }

}
