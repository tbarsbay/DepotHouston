package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsByRouteUseCase;
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
        return new GetStopsByRouteUseCase(stopId, stopRepository,
                threadExecutor, postExecutionThread);
    }
}
