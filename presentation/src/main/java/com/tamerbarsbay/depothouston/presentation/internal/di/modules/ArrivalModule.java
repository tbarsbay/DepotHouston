package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStop;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesByStop;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ArrivalModule {

    public ArrivalModule() {}

    @Provides
    @PerActivity
    GetArrivalsByStop provideGetArrivalsByStopUseCase(ArrivalRepository arrivalRepository,
                                                      ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread) {
        return new GetArrivalsByStop(arrivalRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetRoutesByStop provideGetRoutesByStopUseCase(RouteRepository routeRepository,
                                                  ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new GetRoutesByStop(routeRepository, threadExecutor, postExecutionThread);
    }
}
