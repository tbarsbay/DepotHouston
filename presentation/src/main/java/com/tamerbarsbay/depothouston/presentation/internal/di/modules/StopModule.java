package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsByRoute;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocationByRoute;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class StopModule {

    private String stopId = "";

    public StopModule() {}

    public StopModule(String stopId) {
        this.stopId = stopId;
    }

    @Provides
    @PerActivity
    @Named("stopsByRoute")
    UseCase provideGetStopsByRouteUseCase(StopRepository stopRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new GetStopsByRoute(stopId, stopRepository,
                threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    @Named("stopsNearLocationByRoute")
    UseCase provideGetStopsNearLocationByRouteUseCase(StopRepository stopRepository,
                                                      ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread) {
        return new GetStopsNearLocationByRoute(stopRepository, threadExecutor, postExecutionThread);
    }
}
