package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public class GetStopsByRouteUseCase extends UseCase {

    private final String routeId;
    private final StopRepository stopRepository;

    @Inject
    public GetStopsByRouteUseCase(String routeId,
                                  StopRepository stopRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeId = routeId;
        this.stopRepository = stopRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.stopRepository.getStopListByRoute(this.routeId);
    }
}
