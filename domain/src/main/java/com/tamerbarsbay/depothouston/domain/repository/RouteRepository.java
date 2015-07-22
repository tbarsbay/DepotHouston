package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Route;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Route} related data.
 * Created by Tamer on 7/22/2015.
 */
public interface RouteRepository {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Route} objects.
     */
    Observable<List<Route>> getRouteList();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Route}.
     *
     * @param routeId The route id used to retrieve route data.
     */
    Observable<Route> getRouteDetails(final String routeId);

}
