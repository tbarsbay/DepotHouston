package com.tamerbarsbay.depothouston.data.repository.datasource;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.net.RestApi;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RouteDataStoreFactory {

    private Context context;
    private final RouteCache routeCache;

    @Inject
    public RouteDataStoreFactory(Context context, RouteCache routeCache) {
        if (context == null || routeCache == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context;
        this.routeCache = routeCache;
    }

    public RouteDataStore create(String routeId) {
        //TODO
        return null;
    }

    public RouteDataStore createCloudDataStore() {
        RestApi restApi = new RestApi(this.context);
        return new CloudRouteDataStore(restApi, this.routeCache);
    }
}
