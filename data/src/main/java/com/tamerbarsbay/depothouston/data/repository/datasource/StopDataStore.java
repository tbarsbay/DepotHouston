package com.tamerbarsbay.depothouston.data.repository.datasource;

import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.util.List;

import rx.Observable;

public interface StopDataStore {

    Observable<List<StopEntity>> getStopListByRoute(final String routeId);

}
