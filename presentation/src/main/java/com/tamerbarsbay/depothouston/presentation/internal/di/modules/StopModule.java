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
    private String direction = "";

    public StopModule() {}

    public StopModule(String stopId, String direction) {
        this.stopId = stopId;
        this.direction = direction;
    }

    @Provides
    @PerActivity
    @Named("stopsByRoute")
    UseCase provideGetStopsByRouteUseCase(StopRepository stopRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new GetStopsByRoute(stopId, direction, stopRepository,
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
