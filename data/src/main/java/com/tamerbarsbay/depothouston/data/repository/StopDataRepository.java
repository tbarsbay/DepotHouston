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

/**
 * Created by Tamer on 7/22/2015.
 */
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
    public Observable<List<Stop>> getStopListByRoute(String routeId) {
        // Stop lists will always come from the Metro API and not the local cache
        final StopDataStore stopDataStore = this.stopDataStoreFactory.createCloudDataStore();
        return stopDataStore.getStopListByRoute(routeId).map(stopEntityListMapper);
    }

    @Override
    public Observable<Stop> getStopDetails(String stopId) {
        final StopDataStore stopDataStore = this.stopDataStoreFactory.create(stopId);
        return null; //TODO getStopDetails method in StopDataStore
    }
}
