package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

public class GetArrivalsByStopAndRoute extends UseCase {

    private String stopId;
    private String routeId;
    private boolean areParametersSet = false;
    private final ArrivalRepository arrivalRepository;

    @Inject
    public GetArrivalsByStopAndRoute(ArrivalRepository arrivalRepository,
                                     ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.arrivalRepository = arrivalRepository;
    }

    public void setParameters(String routeId, String stopId) {
        this.routeId = routeId;
        this.stopId = stopId;
        areParametersSet = true;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        //TODO move the houstonmetroapi.stopsNearLocationByRoute stuff to a use case to stay consistent
        return arrivalRepository
                .arrivalsByStop(stopId)
                .flatMap(new Func1<List<Arrival>, Observable<?>>() {
                    @Override
                    public Observable<?> call(List<Arrival> arrivals) {
                        return Observable.from(arrivals).filter(new Func1<Arrival, Boolean>() {
                            // Filter for arrivals of only the given route
                            @Override
                            public Boolean call(Arrival arrival) {
                                return arrival.getRouteId().equals(routeId);
                            }
                        });
                    }
                })
                .take(3)
                .toList();
    }
}
