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
    static final String AUTH_KEY_PARAM = "&subscription-key=";
    static final String AUTH_KEY_VALUE = "baee3193bf9849f58d0ce02feb3ca7c3"; //TODO temp remove this

    static final String EXTRAS = "?" + JSON_FORMAT_PARAM + AUTH_KEY_PARAM + AUTH_KEY_VALUE; //TODO temp

    static final String API_URL_GET_ROUTE_LIST = METRO_API_BASE_URL + "Routes" + EXTRAS;

    static final String API_URL_GET_ROUTE_DETAILS = METRO_API_BASE_URL + "Routes('%s')" + EXTRAS;

    static final String API_URL_GET_STOPS_BY_ROUTE = METRO_API_BASE_URL + "Routes('%s')/Stops" + EXTRAS;

    Observable<List<RouteEntity>> getRouteList();

    Observable<RouteEntity> getRouteDetails(final String routeId);

    Observable<List<StopEntity>> getStopsByRoute(final String routeId);
}
