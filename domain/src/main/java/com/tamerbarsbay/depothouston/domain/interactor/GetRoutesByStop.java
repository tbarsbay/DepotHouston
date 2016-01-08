package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetRoutesByStop extends UseCase {

    private String stopId;
    private boolean areParametersSet = false;
    private final RouteRepository routeRepository;

    @Inject
    public GetRoutesByStop(RouteRepository routeRepository,
                           ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeRepository = routeRepository;
    }

    public void setParameters(String stopId) {
        this.stopId = stopId;
        areParametersSet = true;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        return routeRepository.routesByStop(stopId);
    }
}
