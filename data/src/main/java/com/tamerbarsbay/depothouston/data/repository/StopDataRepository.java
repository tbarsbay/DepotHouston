package com.tamerbarsbay.depothouston.data.repository;

import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.StopEntityDataMapper;
import com.tamerbarsbay.depothouston.data.repository.datasource.StopDataStore;
import com.tamerbarsbay.depothouston.data.repository.datasource.StopDataStoreFactory;
import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class StopDataRepository implements StopRepository {

    private final StopDataStoreFactory stopDataStoreFactory;
    private final StopEntityDataMapper stopEntityDataMapper;

    private final Func1<List<StopEntity>, List<Stop>> stopEntityListMapper =
        new Func1<List<StopEntity>, List<Stop>>() {
            @Override
            public List<Stop> call(List<StopEntity> stopEntities) {
                return StopDataRepository.this.stopEntityDataMapper.transform(stopEntities);
            }
        };

    private final Func1<StopEntity, Stop> stopEntityMapper =
        new Func1<StopEntity, Stop>() {
            @Override
            public Stop call(StopEntity stopEntity) {
                return StopDataRepository.this.stopEntityDataMapper.transform(stopEntity);
            }
        };

    @Inject
    public StopDataRepository(
            StopDataStoreFactory stopDataStoreFactory,
            StopEntityDataMapper stopEntityDataMapper) {
        this.stopDataStoreFactory = stopDataStoreFactory;
        this.stopEntityDataMapper = stopEntityDataMapper;
    }

    @Override
    public Observable<List<Stop>> stopsByRoute(String routeId, String direction) {
        // Stop lists will always come from the Metro API and not the local cache
        final StopDataStore stopDataStore = this.stopDataStoreFactory.createCloudDataStore();
        return stopDataStore.stopsByRoute(routeId, direction).map(stopEntityListMapper);
    }

    @Override
    public Observable<Stop> stop(String stopId) {
        final StopDataStore stopDataStore = this.stopDataStoreFactory.create(stopId);
        return null; //TODO stop method in StopDataStore
    }

    @Override
    public Observable<List<Stop>> stopsNearLocation(double lat, double lon, String radiusInMiles) {
        final StopDataStore stopDataStore = this.stopDataStoreFactory.createCloudDataStore();
        return stopDataStore.stopsNearLocation(lat, lon, radiusInMiles).map(stopEntityListMapper);
    }

    @Override
    public Observable<List<Stop>> stopsNearLocationByRoute(String routeId, double lat, double lon, String radiusInMiles) {
        final StopDataStore stopDataStore = this.stopDataStoreFactory.createCloudDataStore();
        return stopDataStore.stopsNearLocationByRoute(routeId, lat, lon, radiusInMiles).map(stopEntityListMapper);
    }
}
