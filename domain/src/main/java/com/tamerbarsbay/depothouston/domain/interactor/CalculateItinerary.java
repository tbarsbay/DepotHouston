package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ItineraryRepository;

import javax.inject.Inject;

import rx.Observable;

public class CalculateItinerary extends UseCase {

    private final double lat1, lon1, lat2, lon2;
    private final ItineraryRepository itineraryRepository;

    @Inject
    public CalculateItinerary(double lat1, double lon1,
                              double lat2, double lon2,
                              ItineraryRepository itineraryRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.itineraryRepository.calculateItinerary(lat1, lon1, lat2, lon2);
    }
}
