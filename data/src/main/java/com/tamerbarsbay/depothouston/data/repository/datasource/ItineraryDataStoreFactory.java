package com.tamerbarsbay.depothouston.data.repository.datasource;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.ItineraryCache;
import com.tamerbarsbay.depothouston.data.entity.mapper.ItineraryEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.net.RestApi;
import com.tamerbarsbay.depothouston.data.net.RestApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/24/2015.
 */
@Singleton
public class ItineraryDataStoreFactory {

    private Context context;
    private final ItineraryCache itineraryCache;

    @Inject
    public ItineraryDataStoreFactory(Context context, ItineraryCache itineraryCache) {
        if (context == null || itineraryCache == null) {
            throw new IllegalArgumentException("Constructor cannot have null parameters.");
        }
        this.context = context;
        this.itineraryCache = itineraryCache;
    }

    public ItineraryDataStore create(String itineraryId) {
        //TODO
        return null;
    }

    public ItineraryDataStore createCloudDataStore() {
        ItineraryEntityJsonMapper itineraryEntityJsonMapper = new ItineraryEntityJsonMapper();
        RestApi restApi = new RestApiImpl(this.context, itineraryEntityJsonMapper);
        return new CloudItineraryDataStore(restApi, this.itineraryCache);
    }

}
