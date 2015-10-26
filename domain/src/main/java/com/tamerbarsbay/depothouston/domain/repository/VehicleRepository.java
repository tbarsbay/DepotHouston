package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Vehicle;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Vehicle} related data.
 * Created by Tamer on 7/24/2015.
 */
public interface VehicleRepository {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Vehicle} objects.
     */
    Observable<List<Vehicle>> vehiclesByRoute(final String routeId);

}
