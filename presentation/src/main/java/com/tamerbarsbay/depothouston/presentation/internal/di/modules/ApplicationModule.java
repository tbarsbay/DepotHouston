package com.tamerbarsbay.depothouston.presentation.internal.di.modules;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.RouteCache;
import com.tamerbarsbay.depothouston.data.cache.RouteCacheImpl;
import com.tamerbarsbay.depothouston.data.cache.StopCache;
import com.tamerbarsbay.depothouston.data.cache.StopCacheImpl;
import com.tamerbarsbay.depothouston.data.executor.JobExecutor;
import com.tamerbarsbay.depothouston.data.repository.RouteDataRepository;
import com.tamerbarsbay.depothouston.data.repository.StopDataRepository;
import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.RouteRepository;
import com.tamerbarsbay.depothouston.domain.repository.StopRepository;
import com.tamerbarsbay.depothouston.presentation.AndroidApplication;
import com.tamerbarsbay.depothouston.presentation.UIThread;
import com.tamerbarsbay.depothouston.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Tamer on 7/23/2015.
 */
@Module
public class ApplicationModule {

    private final AndroidApplication application;

    public ApplicationModule(AndroidApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return this.application;
    }

    @Provides
    @Singleton
    Navigator provideNavigator() {
        return new Navigator();
    }

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @Singleton
    StopCache provideStopCache(StopCacheImpl stopCache) {
        return stopCache;
    }

    @Provides
    @Singleton
    RouteCache provideRouteCache(RouteCacheImpl routeCache) {
        return routeCache;
    }

    @Provides
    @Singleton
    RouteRepository provideRouteRepository(RouteDataRepository routeDataRepository) {
        return routeDataRepository;
    }

    @Provides
    @Singleton
    StopRepository provideStopRepository(StopDataRepository stopDataRepository) {
        return stopDataRepository;
    }

}
