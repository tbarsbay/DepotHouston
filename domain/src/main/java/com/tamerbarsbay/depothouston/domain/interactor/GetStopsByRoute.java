package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetStopsByRoute extends UseCase {

    private String routeId;
    private String direction;
    private final StopRepository stopRepository;
    public boolean areParametersSet = false;

    @Inject
    public GetStopsByRoute(StopRepository stopRepository,
                           ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.stopRepository = stopRepository;
    }

    public void setParameters(String routeId, String direction) {
        this.routeId = routeId;
        this.direction = direction;
        areParametersSet = routeId != null && direction != null;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        return this.stopRepository.stopsByRoute(this.routeId, this.direction);
    }
}
