package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStop;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesByStop;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ArrivalModule {

    private String stopId = "";

    public ArrivalModule() {}

    public ArrivalModule(String stopId) {
        this.stopId = stopId;
    }

    @Provides
    @PerActivity
    @Named("arrivalsByStop")
    UseCase provideGetArrivalsByStopUseCase(ArrivalRepository arrivalRepository,
                                            ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread) {
        return new GetArrivalsByStop(stopId, arrivalRepository,
                threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetRoutesByStop provideGetRoutesByStopUseCase(RouteRepository routeRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new GetRoutesByStop(routeRepository, threadExecutor, postExecutionThread);
    }
}
