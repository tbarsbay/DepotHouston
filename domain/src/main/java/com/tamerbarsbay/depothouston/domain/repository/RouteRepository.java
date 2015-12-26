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
     * Get an {@link rx.Observable} which will emit a List of {@link Route} objects
     * representing all the routes serviced by the Houston Metro.
     */
    Observable<List<Route>> routes();

    /**
     * Get an {@link rx.Observable} which will emit a {@link Route}.
     *
     * @param routeId The route id used to retrieve route data.
     */
    Observable<Route> route(final String routeId);

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Route} objects
     * within a certain mile radius of a given location.
     * @param lat
     * @param lon
     * @param radiusInMiles
     * @return
     */
    Observable<List<Route>> routesNearLocation(final double lat,
                                               final double lon,
                                               final String radiusInMiles);

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Route} objects
     * that are serviced at a given stop.
     * @param stopId The id of the stop for which to get routes.
     * @return
     */
    Observable<List<Route>> routesByStop(final String stopId);

}
