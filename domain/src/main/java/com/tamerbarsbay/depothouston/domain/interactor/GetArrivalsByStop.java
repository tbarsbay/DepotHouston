package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetArrivalsByStop extends UseCase {

    private final String stopId;
    private final ArrivalRepository arrivalRepository;

    @Inject
    public GetArrivalsByStop(String stopId,
                             ArrivalRepository arrivalRepository,
                             ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.stopId = stopId;
        this.arrivalRepository = arrivalRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.arrivalRepository.arrivalsByStop(this.stopId);
    }
}
