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
    Observable<List<Stop>> getStopListByRoute(final String routeId);

    /**
     * Get an {@link rx.Observable} which will emit a {@link Stop}.
     *
     * @param stopId The stop id used to retrieve stop data.
     */
    Observable<Stop> getStopDetails(final String stopId);

}
