package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.VehicleRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetVehiclesByRoute extends UseCase {

    private final String routeId;
    private final VehicleRepository vehicleRepository;

    @Inject
    public GetVehiclesByRoute(String routeId,
                              VehicleRepository vehicleRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeId = routeId;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.vehicleRepository.vehiclesByRoute(this.routeId);
    }
}
