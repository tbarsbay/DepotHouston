package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.util.List;

import rx.Observable;

public interface StopDataStore {

    Observable<List<StopEntity>> stopsByRoute(final String routeId);

    Observable<List<StopEntity>> stopsNearLocation(final double lat,
                                                   final double lon,
                                                   final String radiusInMiles);

    Observable<List<StopEntity>> stopsNearLocationByRoute(final String routeId,
                                                          final double lat,
                                                          final double lon,
                                                          final String radiusInMiles);

}
