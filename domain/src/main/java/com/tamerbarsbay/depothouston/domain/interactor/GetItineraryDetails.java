package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ItineraryRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetItineraryDetails extends UseCase {

    private final String itineraryId;
    private final ItineraryRepository itineraryRepository;

    @Inject
    public GetItineraryDetails(String itineraryId,
                               ItineraryRepository itineraryRepository,
                               ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.itineraryId = itineraryId;
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.itineraryRepository.itinerary(this.itineraryId);
    }
}
