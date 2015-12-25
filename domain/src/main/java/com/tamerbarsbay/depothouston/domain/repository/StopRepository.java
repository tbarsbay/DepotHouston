package com.tamerbarsbay.depothouston.domain.repository;

import com.tamerbarsbay.depothouston.domain.Stop;

import java.util.List;

import rx.Observable;

/**
 * Interface that represents a Repository for getting {@link Stop} related data.
 * Created by Tamer on 7/22/2015.
 */
public interface StopRepository {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Stop} objects.
     */
    Observable<List<Stop>> stopsByRoute(final String routeId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link Stop}.
     *
     * @param stopId The stop id used to retrieve stop data.
     */
    Observable<Stop> stop(final String stopId);

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Stop} objects representing
     * all transit stops within a given number of miles from the coordinates provided.
     * @param lat Latitude coordinate of the center location.
     * @param lon Longitude coordinate of the center location.
     * @param radiusInMiles Number of miles within which you want to find stops.
     * @return
     */
    Observable<List<Stop>> stopsNearLocation(final double lat,
                                             final double lon,
                                             final String radiusInMiles);
    /**
     * Get an {@link rx.Observable} which will emit a List of {@link Stop} objects representing
     * all transit stops of a given route within a given number of miles from the coordinates
     * provided.
     * @param lat Latitude coordinate of the center location.
     * @param lon Longitude coordinate of the center location.
     * @param radiusInMiles Number of miles within which you want to find stops.
     * @return
     */
    Observable<List<Stop>> stopsNearLocationByRoute(final String routeId,
                                                    final double lat,
                                                    final double lon,
                                                    final String radiusInMiles);

}
