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

    // Used for building Uris for querying the Houston Metro APIs
    static final String SCHEME_HTTPS = "https";
    static final String AUTHORITY_METRO = "api.ridemetro.org";
    static final String AUTHORITY_COMMUTER = "services.commuterapi.com";
    static final String PATH_TRANSIT_SERVICE = "TransitODataService.svc";
    static final String PATH_DATA = "data";
    static final String PATH_ROUTES = "Routes";
    static final String PATH_ROUTES_WITH_ID = "Routes(\'%s\')";
    static final String PATH_STOPS = "Stops";

    static final String PARAM_KEY_FORMAT = "$format";
    static final String PARAM_JSON = "json";
    static final String PARAM_KEY_AUTH_TOKEN = "subscription-key";
    static final String PARAM_AUTH_TOKEN = "baee3193bf9849f58d0ce02feb3ca7c3"; //TODO remove later
    static final String PARAM_KEY_FILTER = "$filter";
    static final String PARAM_FILTER_DIRECTION = "DirectionId eq \'%s_%s\'";

    Observable<List<RouteEntity>> getRouteList();

    Observable<RouteEntity> getRouteDetails(final String routeId);

    Observable<List<StopEntity>> getStopsByRoute(final String routeId);
}
