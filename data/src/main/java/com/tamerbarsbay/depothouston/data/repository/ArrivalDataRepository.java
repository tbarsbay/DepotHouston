package com.tamerbarsbay.depothouston.data.repository;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.ArrivalEntityDataMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.ArrivalEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.net.RestApi;
import com.tamerbarsbay.depothouston.data.net.RestApiImpl;
import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Tamer on 7/24/2015.
 */
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

    @Override
    public Observable<List<Arrival>> getArrivalsByStop(String stopId) {
        // Arrival lists will always come from the Metro API and not the local cache.
        // Therefore we can skip the creation of an ArrivalDataStore and just use the RestApi.
        ArrivalEntityJsonMapper arrivalEntityJsonMapper = new ArrivalEntityJsonMapper();
        RestApi restApi = new RestApiImpl(this.context, arrivalEntityJsonMapper);
        return restApi.getArrivalsByStop(stopId).map(arrivalEntityListMapper);
    }

}
