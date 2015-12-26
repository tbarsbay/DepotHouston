package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetArrivalsByStopAndRoute extends UseCase {

    private String stopId;
    private String routeId;
    private boolean areParametersSet = false;
    private final ArrivalRepository arrivalRepository;

    @Inject
    public GetArrivalsByStopAndRoute(ArrivalRepository arrivalRepository,
                                     ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.arrivalRepository = arrivalRepository;
    }

    public void setParameters(String routeId, String stopId) {
        this.routeId = routeId;
        this.stopId = stopId;
        areParametersSet = true;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        //TODO filter by route
        //TODO only take up to 3
        return arrivalRepository.arrivalsByStop(stopId);
    }
}
