package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocation;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class MapSearchModule {

    public MapSearchModule() {}

    @Provides
    @PerActivity
    GetStopsNearLocation provideGetStopsNearLocationUseCase(StopRepository stopRepository,
                                               ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread) {
        return new GetStopsNearLocation(stopRepository, threadExecutor, postExecutionThread);
    }
}
