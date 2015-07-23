package com.tamerbarsbay.depothouston.data.net;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public interface RestApi {

    //TODO docs

    static final String METRO_API_BASE_URL = "https://api.ridemetro.org/data/";
    static final String COMMUTER_API_BASE_URL = "http://services.commuterapi.com/TransitODataService.svc/";

    static final String JSON_FORMAT_PARAM = "$format=json";

    static final String API_URL_GET_ROUTE_LIST = METRO_API_BASE_URL + "Routes?" + JSON_FORMAT_PARAM;

    static final String API_URL_GET_ROUTE_DETAILS = METRO_API_BASE_URL + "Routes('%s')?" + JSON_FORMAT_PARAM;

    static final String API_URL_GET_STOPS_BY_ROUTE = METRO_API_BASE_URL + "Routes('%s')/Stops?" + JSON_FORMAT_PARAM;

    Observable<List<RouteEntity>> getRouteList();

    Observable<RouteEntity> getRouteDetails(final String routeId);

    Observable<List<StopEntity>> getStopsByRoute(final String routeId);
}
