package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.ArrivalRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetArrivalsByStop extends UseCase {

    private String stopId;
    private ArrivalRepository arrivalRepository;
    public boolean areParametersSet = false;

    @Inject
    public GetArrivalsByStop(ArrivalRepository arrivalRepository,
                             ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.arrivalRepository = arrivalRepository;
    }

    public void setParameters(String stopId) {
        this.stopId = stopId;
        areParametersSet = stopId != null;
    }

    @Override
    public Observable buildUseCaseObservable() {
        if (!areParametersSet) {
            throw new IllegalStateException("Required parameters have not been set.");
        }
        return this.arrivalRepository.arrivalsByStop(this.stopId);
    }
}
