package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetRouteDetails extends UseCase {

    private final String routeId;
    private final RouteRepository routeRepository;

    @Inject
    public GetRouteDetails(String routeId,
                           RouteRepository routeRepository,
                           ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeId = routeId;
        this.routeRepository = routeRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.routeRepository.route(this.routeId);
    }
}
