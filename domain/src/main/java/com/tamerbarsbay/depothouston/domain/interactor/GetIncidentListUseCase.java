package com.tamerbarsbay.depothouston.domain.interactor;

import com.tamerbarsbay.depothouston.domain.executor.PostExecutionThread;
import com.tamerbarsbay.depothouston.domain.executor.ThreadExecutor;
import com.tamerbarsbay.depothouston.domain.repository.IncidentRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Tamer on 7/24/2015.
 */
public class GetIncidentListUseCase extends UseCase {

    private final IncidentRepository incidentRepository;

    @Inject
    public GetIncidentListUseCase(IncidentRepository incidentRepository,
                                  ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.incidentRepository = incidentRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return this.incidentRepository.getIncidents();
    }
}
