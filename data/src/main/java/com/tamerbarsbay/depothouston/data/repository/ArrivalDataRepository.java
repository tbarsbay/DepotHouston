package com.tamerbarsbay.depothouston.data.repository;

import android.content.Context;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.ArrivalEntityDataMapper;
import com.tamerbarsbay.depothouston.data.net.HoustonMetroApi;
import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class ArrivalDataRepository implements ArrivalRepository {

    private Context context;
    private ArrivalEntityDataMapper arrivalEntityDataMapper;

    private final Func1<List<ArrivalEntity>, List<Arrival>> arrivalEntityListMapper =
            new Func1<List<ArrivalEntity>, List<Arrival>>() {
                @Override
                public List<Arrival> call(List<ArrivalEntity> arrivalEntities) {
                    return ArrivalDataRepository.this.arrivalEntityDataMapper.transform(arrivalEntities);
                }
            };

    private final Func1<ArrivalEntity, Arrival> arrivalEntityMapper =
            new Func1<ArrivalEntity, Arrival>() {
                @Override
                public Arrival call(ArrivalEntity arrivalEntity) {
                    return ArrivalDataRepository.this.arrivalEntityDataMapper.transform(arrivalEntity);
                }
            };

    @Inject
    public ArrivalDataRepository(Context context, ArrivalEntityDataMapper arrivalEntityDataMapper) {
        this.context = context;
        this.arrivalEntityDataMapper = arrivalEntityDataMapper;
    }

    @RxLogObservable
    @Override
    public Observable<List<Arrival>> arrivalsByStop(String stopId) {
        // Arrival lists will always come from the Metro API and not the local cache.
        // Therefore we can skip the creation of an ArrivalDataStore and just use the RestApi.
        HoustonMetroApi houstonMetroApi = new HoustonMetroApi(this.context);
        return houstonMetroApi.arrivalsByStop(stopId).map(arrivalEntityListMapper);
    }

}
