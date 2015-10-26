package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetRouteDetails;
import com.tamerbarsbay.depothouston.domain.interactor.GetRouteList;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class RouteModule {

    private String routeId = "";

    public RouteModule() {}

    public RouteModule(String routeId) {
        this.routeId = routeId;
    }

    @Provides
    @PerActivity
    @Named("routeList")
    UseCase provideGetRouteListUseCase(GetRouteList getRouteList) {
        return getRouteList;
    }

    @Provides
    @PerActivity
    @Named("routeDetails")
    UseCase provideGetRouteDetailsUseCase(RouteRepository routeRepository,
                                          ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread) {
        return new GetRouteDetails(this.routeId, routeRepository,
                threadExecutor, postExecutionThread);
    }

}
