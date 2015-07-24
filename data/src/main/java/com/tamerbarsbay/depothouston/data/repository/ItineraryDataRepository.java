package com.tamerbarsbay.depothouston.data.repository;

import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.ItineraryEntityDataMapper;
import com.tamerbarsbay.depothouston.data.repository.datasource.ItineraryDataStore;
import com.tamerbarsbay.depothouston.data.repository.datasource.ItineraryDataStoreFactory;
import com.tamerbarsbay.depothouston.domain.Itinerary;
import com.tamerbarsbay.depothouston.domain.repository.ItineraryRepository;

import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Tamer on 7/24/2015.
 */
@Singleton
public class ItineraryDataRepository implements ItineraryRepository {

    ItineraryDataStoreFactory itineraryDataStoreFactory;
    ItineraryEntityDataMapper itineraryEntityDataMapper;

    private final Func1<ItineraryEntity, Itinerary> itineraryEntityMapper =
            new Func1<ItineraryEntity, Itinerary>() {
                @Override
                public Itinerary call(ItineraryEntity itineraryEntity) {
                    return ItineraryDataRepository.this
                            .itineraryEntityDataMapper.transform(itineraryEntity);
                }
            };

    @Override
    public Observable<Itinerary> calculateItinerary(double lat1, double lon1, double lat2, double lon2) {
        final ItineraryDataStore itineraryDataStore =
                this.itineraryDataStoreFactory.createCloudDataStore();
        return itineraryDataStore.calculateItinerary(lat1, lon1, lat2, lon2).map(itineraryEntityMapper);
    }

    @Override
    public Observable<Itinerary> calculateItineraryWithEndTime(double lat1, double lon1, double lat2, double lon2, String endTime) {
        final ItineraryDataStore itineraryDataStore =
                this.itineraryDataStoreFactory.createCloudDataStore();
        return itineraryDataStore.calculateItineraryWithEndTime(lat1, lon1, lat2, lon2, endTime)
                .map(itineraryEntityMapper);
    }

    @Override
    public Observable<Itinerary> getItineraryDetails(String itineraryId) {
        final ItineraryDataStore itineraryDataStore =
                this.itineraryDataStoreFactory.create(itineraryId);
        return itineraryDataStore.getItineraryDetails(itineraryId).map(itineraryEntityMapper);
    }
}
