package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetStopsNearLocation extends UseCase {

    private double lat;
    private double lon;
    private String radiusInMiles;
    private boolean areParametersSet = false;
    private final StopRepository stopRepository;

    @Inject
    public GetStopsNearLocation(StopRepository stopRepository,
                                ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.stopRepository = stopRepository;
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
        return stopRepository.stopsNearLocation(lat, lon, radiusInMiles);
    }
}
