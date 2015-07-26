package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStopUseCase;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tamer on 7/26/2015.
 */
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
        return new GetArrivalsByStopUseCase(stopId, arrivalRepository,
                threadExecutor, postExecutionThread);
    }
}
