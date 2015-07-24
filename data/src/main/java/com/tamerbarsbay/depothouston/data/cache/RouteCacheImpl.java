package com.tamerbarsbay.depothouston.data.cache;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.serializer.JsonSerializer;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Tamer on 7/23/2015.
 */
public class RouteCacheImpl extends Cache implements RouteCache {

    private static final String FILE_NAME_PREFIX = "route_";

    @Inject
    public RouteCacheImpl(Context context, JsonSerializer serializer,
                          FileManager fileManager, ThreadExecutor threadExecutor) {
        super(context, serializer, fileManager, threadExecutor);
    }

    @Override
    public Observable<RouteEntity> get(final String routeId) {
        return Observable.create(new Observable.OnSubscribe<RouteEntity>() {
            @Override
            public void call(Subscriber<? super RouteEntity> subscriber) {
                File routeEntityFile = RouteCacheImpl.this.buildFile(routeId, FILE_NAME_PREFIX);
                String fileContent =
                        RouteCacheImpl.this.fileManager.readFileContent(routeEntityFile);
                RouteEntity routeEntity =
                        RouteCacheImpl.this.serializer.deserializeRoute(fileContent);

                if (routeEntity != null) {
                    subscriber.onNext(routeEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("Route not found.")); //TODO temp
                }
            }
        });
    }

    @Override
    public void put(RouteEntity routeEntity) {
        if (routeEntity != null) {
            File routeEntityFile = this.buildFile(routeEntity.getRouteId(), FILE_NAME_PREFIX);
            if (!isCached(routeEntity.getRouteId(), FILE_NAME_PREFIX)) {
                String jsonString = this.serializer.serialize(routeEntity);
                this.executeAsynchronously(new CacheWriter(this.fileManager, routeEntityFile,
                        jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }
}
