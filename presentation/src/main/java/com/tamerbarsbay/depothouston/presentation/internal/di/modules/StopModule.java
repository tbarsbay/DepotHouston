package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsByRoute;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocationByRoute;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class StopModule {

    public StopModule() {}

    @Provides
    @PerActivity
    GetStopsByRoute provideGetStopsByRouteUseCase(StopRepository stopRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new GetStopsByRoute(stopRepository,
                threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetStopsNearLocationByRoute provideGetStopsNearLocationByRouteUseCase(StopRepository stopRepository,
                                                      ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread) {
        return new GetStopsNearLocationByRoute(stopRepository, threadExecutor, postExecutionThread);
    }
}
