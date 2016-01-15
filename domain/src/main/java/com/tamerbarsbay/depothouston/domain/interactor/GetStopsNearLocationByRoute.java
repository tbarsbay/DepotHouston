package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetStopsNearLocationByRoute extends UseCase {

    private String routeId;
    private String direction;
    private double lat;
    private double lon;
    private String radiusInMiles;
    private boolean areParametersSet = false;
    private final StopRepository stopRepository;

    @Inject
    public GetStopsNearLocationByRoute(StopRepository stopRepository,
                                       ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.stopRepository = stopRepository;
    }

    public void setParameters(String routeId, String direction, double lat, double lon, String radiusInMiles) {
        this.routeId = routeId;
        this.direction = direction;
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;
        areParametersSet = routeId != null && direction != null && radiusInMiles != null;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        return stopRepository.stopsNearLocationByRoute(routeId, direction, lat, lon, radiusInMiles);
    }
}
