package com.tamerbarsbay.depothouston.data.cache;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.serializer.JsonSerializer;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Tamer on 7/23/2015.
 */
public class StopCacheImpl extends Cache implements StopCache {

    private static final String FILE_NAME_PREFIX = "stop_";

    @Inject
    public StopCacheImpl(Context context, JsonSerializer serializer,
                         FileManager fileManager, ThreadExecutor threadExecutor) {
        super(context, serializer, fileManager, threadExecutor);
    }

    @Override
    public Observable<StopEntity> get(final String stopId) {
        return Observable.create(new Observable.OnSubscribe<StopEntity>() {
            @Override
            public void call(Subscriber<? super StopEntity> subscriber) {
                File stopEntityFile = StopCacheImpl.this.buildFile(stopId, FILE_NAME_PREFIX);
                String fileContent =
                        StopCacheImpl.this.fileManager.readFileContent(stopEntityFile);
                StopEntity stopEntity =
                        StopCacheImpl.this.serializer.deserializeStop(fileContent);

                if (stopEntity != null) {
                    subscriber.onNext(stopEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("Stop not found.")); //TODO temp
                }
            }
        });
    }

    @Override
    public void put(StopEntity stopEntity) {
        if (stopEntity != null) {
            File stopEntityFile = this.buildFile(stopEntity.getStopId(), FILE_NAME_PREFIX);
            if (!isCached(stopEntity.getStopId(), FILE_NAME_PREFIX)) {
                String jsonString = this.serializer.serialize(stopEntity);
                this.executeAsynchronously(new CacheWriter(this.fileManager, stopEntityFile,
                        jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }
}
