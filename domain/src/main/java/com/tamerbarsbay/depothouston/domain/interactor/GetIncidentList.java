package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.IncidentRepository;

import javax.inject.Inject;

import rx.Observable;

public class GetIncidentList extends UseCase {

    private final IncidentRepository incidentRepository;

    @Inject
    public GetIncidentList(IncidentRepository incidentRepository,
                           ThreadExecutor threadExecutor,
                           PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.incidentRepository = incidentRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.incidentRepository.incidents();
    }
}
