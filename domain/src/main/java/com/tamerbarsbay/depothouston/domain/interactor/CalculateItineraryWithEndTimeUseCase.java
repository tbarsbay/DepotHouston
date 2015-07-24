package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ItineraryRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Tamer on 7/24/2015.
 */
public class CalculateItineraryWithEndTimeUseCase extends UseCase {

    private final double lat1, lon1, lat2, lon2;
    private final String endTime;
    private final ItineraryRepository itineraryRepository;

    @Inject
    public CalculateItineraryWithEndTimeUseCase(double lat1, double lon1,
                                                double lat2, double lon2,
                                                String endTime,
                                                ItineraryRepository itineraryRepository,
                                                ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.lat1 = lat1;
        this.lon1 = lon1;
        this.lat2 = lat2;
        this.lon2 = lon2;
        this.endTime = endTime;
        this.itineraryRepository = itineraryRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.itineraryRepository.calculateItineraryWithEndTime(
                lat1, lon1, lat2, lon2, endTime);
    }
}
