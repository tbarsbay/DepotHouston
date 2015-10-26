package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetStopsNearLocation extends UseCase {

    private final double lat;
    private final double lon;
    private final String radiusInMiles;
    private final StopRepository stopRepository;

    @Inject
    public GetStopsNearLocation(double lat,
                                double lon,
                                String radiusInMiles,
                                StopRepository stopRepository,
                                ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;
        this.stopRepository = stopRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.stopRepository.stopsNearLocation(lat, lon, radiusInMiles);
    }
}
