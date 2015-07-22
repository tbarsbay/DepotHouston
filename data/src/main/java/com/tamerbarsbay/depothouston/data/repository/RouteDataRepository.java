package com.tamerbarsbay.depothouston.data.repository;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.RouteEntityDataMapper;
import com.tamerbarsbay.depothouston.data.repository.datasource.RouteDataStore;
import com.tamerbarsbay.depothouston.data.repository.datasource.RouteDataStoreFactory;
import com.tamerbarsbay.depothouston.domain.Route;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Tamer on 7/22/2015.
 */
@Singleton
public class RouteDataRepository implements RouteRepository {

    private final RouteDataStoreFactory routeDataStoreFactory;
    private final RouteEntityDataMapper routeEntityDataMapper;

    private final Func1<List<RouteEntity>, List<Route>> routeEntityListMapper =
        new Func1<List<RouteEntity>, List<Route>>() {
            @Override
            public List<Route> call(List<RouteEntity> routeEntities) {
                return RouteDataRepository.this.routeEntityDataMapper.transform(routeEntities);
            }
        };

    private final Func1<RouteEntity, Route> routeEntityMapper =
        new Func1<RouteEntity, Route>() {
            @Override
            public Route call(RouteEntity routeEntity) {
                return RouteDataRepository.this.routeEntityDataMapper.transform(routeEntity);
            }
        };

    @Inject
    public RouteDataRepository(
            RouteDataStoreFactory routeDataStoreFactory,
            RouteEntityDataMapper routeEntityDataMapper) {
        this.routeDataStoreFactory = routeDataStoreFactory;
        this.routeEntityDataMapper = routeEntityDataMapper;
    }

    @Override
    public Observable<List<Route>> getRouteList() {
        // Route lists will always come from the Metro API and not the local cache
        final RouteDataStore routeDataStore = this.routeDataStoreFactory.createCloudDataStore();
        return routeDataStore.getRouteList().map(routeEntityListMapper);
    }

    @Override
    public Observable<Route> getRouteDetails(String routeId) {
        final RouteDataStore routeDataStore = this.routeDataStoreFactory.create(routeId);
        return routeDataStore.getRouteDetails(routeId).map(routeEntityMapper);
    }
}
