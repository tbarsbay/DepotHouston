package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import java.util.List;

import rx.Observable;

public interface RouteDataStore {

    Observable<List<RouteEntity>> routes();

    Observable<RouteEntity> route(final String routeId);

    Observable<List<RouteEntity>> routesNearLocation(final double lat,
                                                     final double lon,
                                                     final String radiusInMiles);

    Observable<List<RouteEntity>> routesByStop(final String stopId);

}
