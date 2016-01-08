package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStopAndRoute;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class WidgetProviderModule {

    public WidgetProviderModule() {}

    @Provides
    @PerActivity
    GetArrivalsByStopAndRoute provideGetArrivalsByStopAndRoute(ArrivalRepository arrivalRepository,
                                                               ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread) {
        return new GetArrivalsByStopAndRoute(arrivalRepository, threadExecutor, postExecutionThread);
    }
}
