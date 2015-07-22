package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public class GetRouteListUseCase extends UseCase {

    private final RouteRepository routeRepository;

    @Inject
    public GetRouteListUseCase(RouteRepository routeRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.routeRepository = routeRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.routeRepository.getRouteList();
    }
}
