package com.tamerbarsbay.depothouston.data.repository;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.VehicleEntityDataMapper;
import com.tamerbarsbay.depothouston.data.net.RestApi;
import com.tamerbarsbay.depothouston.domain.Vehicle;
import com.tamerbarsbay.depothouston.domain.repository.VehicleRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class VehicleDataRepository implements VehicleRepository {

    Context context;
    VehicleEntityDataMapper vehicleEntityDataMapper;

    private final Func1<List<VehicleEntity>, List<Vehicle>> vehicleEntityListMapper =
            new Func1<List<VehicleEntity>, List<Vehicle>>() {
                @Override
                public List<Vehicle> call(List<VehicleEntity> vehicleEntities) {
                    return VehicleDataRepository.this.vehicleEntityDataMapper.transform(vehicleEntities);
                }
            };

    private final Func1<VehicleEntity, Vehicle> vehicleEntityMapper =
            new Func1<VehicleEntity, Vehicle>() {
                @Override
                public Vehicle call(VehicleEntity vehicleEntity) {
                    return VehicleDataRepository.this.vehicleEntityDataMapper.transform(vehicleEntity);
                }
            };

    @Inject
    public VehicleDataRepository(Context context, VehicleEntityDataMapper vehicleEntityDataMapper) {
        this.context = context;
        this.vehicleEntityDataMapper = vehicleEntityDataMapper;
    }

    @Override
    public Observable<List<Vehicle>> vehiclesByRoute(String routeId) {
        // Vehicle lists will always come from the Metro API and not the local cache.
        // Therefore we can skip the creation of an VehicleDataStore and just use the RestApi.
        RestApi restApi = new RestApi(this.context);
        return restApi.vehiclesByRoute(routeId).map(vehicleEntityListMapper);
    }
}
