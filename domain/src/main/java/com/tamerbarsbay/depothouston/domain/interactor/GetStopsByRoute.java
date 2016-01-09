package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetStopsByRoute extends UseCase {

    private final String routeId;
    private final String direction;
    private final StopRepository stopRepository;

    @Inject
    public GetStopsByRoute(String routeId,
                           String direction,
                           StopRepository stopRepository,
                           ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeId = routeId;
        this.direction = direction;
        this.stopRepository = stopRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.stopRepository.stopsByRoute(this.routeId, this.direction);
    }
}
