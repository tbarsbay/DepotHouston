package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface RouteDataStore {

    Observable<List<RouteEntity>> getRouteList();

    Observable<RouteEntity> getRouteDetails(final String routeId);

}
