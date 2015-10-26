package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Arrival;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Arrival} related data.
 * Created by Tamer on 7/24/2015.
 */
public interface ArrivalRepository {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Arrival} objects.
     */
    Observable<List<Arrival>> arrivalsByStop(final String stopId);

    //TODO getArrivalsByVehicle? for vehicle following?

}
