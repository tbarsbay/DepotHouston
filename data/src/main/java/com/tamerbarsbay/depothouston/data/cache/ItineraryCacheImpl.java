package com.tamerbarsbay.depothouston.data.cache;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.cache.serializer.JsonSerializer;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;

import java.io.File;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

/**
 * Adapted from https://github.com/android10/Android-CleanArchitecture/
 */
public class ItineraryCacheImpl extends Cache implements ItineraryCache {

    private static final String FILE_NAME_PREFIX = "itinerary_";

    @Inject
    public ItineraryCacheImpl(Context context, JsonSerializer serializer,
                              FileManager fileManager, ThreadExecutor executor) {
        super(context, serializer, fileManager, executor);
    }

    @Override
    public synchronized Observable<ItineraryEntity> get(final String itineraryId) {
        return Observable.create(new Observable.OnSubscribe<ItineraryEntity>() {
            @Override
            public void call(Subscriber<? super ItineraryEntity> subscriber) {
                File itineraryEntityFile = ItineraryCacheImpl.this.buildFile(itineraryId, FILE_NAME_PREFIX);
                String fileContent =
                        ItineraryCacheImpl.this.fileManager.readFileContent(itineraryEntityFile);
                ItineraryEntity itineraryEntity =
                        ItineraryCacheImpl.this.serializer.deserializeItinerary(fileContent);

                if (itineraryEntity != null) {
                    subscriber.onNext(itineraryEntity);
                    subscriber.onCompleted();
                } else {
                    subscriber.onError(new Exception("Itinerary not found.")); //TODO temp
                }
            }
        });
    }

    @Override
    public synchronized void put(ItineraryEntity itineraryEntity) {
        if (itineraryEntity != null) {
            File itineraryEntityFile = this.buildFile(itineraryEntity.getItineraryId(), FILE_NAME_PREFIX);
            if (!isCached(itineraryEntity.getItineraryId(), FILE_NAME_PREFIX)) {
                String jsonString = this.serializer.serialize(itineraryEntity);
                this.executeAsynchronously(new CacheWriter(this.fileManager, itineraryEntityFile,
                        jsonString));
                setLastCacheUpdateTimeMillis();
            }
        }
    }
}
