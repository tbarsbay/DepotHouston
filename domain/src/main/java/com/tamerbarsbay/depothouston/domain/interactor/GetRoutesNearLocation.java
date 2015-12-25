package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetRoutesNearLocation extends UseCase {

    private double lat;
    private double lon;
    private String radiusInMiles;
    private boolean areParametersSet = false;
    private final RouteRepository routeRepository;

    @Inject
    public GetRoutesNearLocation(RouteRepository routeRepository,
                                 ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeRepository = routeRepository;
    }

    public void setParameters(double lat, double lon, String radiusInMiles) {
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;
        areParametersSet = true;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        return routeRepository.routesNearLocation(lat, lon, radiusInMiles);
    }
}
