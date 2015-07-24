package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Incident;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Incident} related data.
 * Created by Tamer on 7/24/2015.
 */
public interface IncidentRepository {

    Observable<List<Incident>> getIncidents();

}
