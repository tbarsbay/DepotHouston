package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocation;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MapSearchModule {

    private double lat;
    private double lon;
    private String radiusInMiles;

    public MapSearchModule() {}

    public MapSearchModule(double lat, double lon, String radiusInMiles) {
        this.lat = lat;
        this.lon = lon;
        this.radiusInMiles = radiusInMiles;
    }

    @Provides
    @PerActivity
    @Named("stopsNearLocation")
    UseCase provideGetStopsNearLocationUseCase(StopRepository stopRepository,
                                               ThreadExecutor threadExecutor,
                                               PostExecutionThread postExecutionThread) {
        return new GetStopsNearLocation(lat, lon, radiusInMiles, stopRepository,
                threadExecutor, postExecutionThread);
    }
}
