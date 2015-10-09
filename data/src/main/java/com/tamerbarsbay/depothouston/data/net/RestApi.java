package com.tamerbarsbay.depothouston.data.net;

import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

public interface RestApi {

    String API_BASE_URL = "https://api.ridemetro.org/data/";
    String CONTENT_TYPE_JSON_HEADER = "Content-Type: application/json; charset=utf-8";
    String AUTH_TOKEN = "baee3193bf9849f58d0ce02feb3ca7c3"; //TODO remove later
    String DEFAULT_PARAMS = "?$format=json&subscription-key=" + AUTH_TOKEN;

    //TODO docs

    // Used for building Uris for querying the Houston Metro APIs
    static final String SCHEME_HTTPS = "https";
    static final String AUTHORITY_METRO = "api.ridemetro.org";
    static final String AUTHORITY_COMMUTER = "services.commuterapi.com";
    static final String PATH_TRANSIT_SERVICE = "TransitODataService.svc";
    static final String PATH_DATA = "data";
    static final String PATH_ROUTES = "Routes";
    static final String PATH_ROUTE_WITH_ID = "Routes(\'%s\')";
    static final String PATH_STOPS = "Stops";
    static final String PATH_STOP_WITH_ID = "Stops(\'%s\')";
    static final String PATH_INCIDENTS = "Incidents";
    static final String PATH_ITINERARY_WITH_ID = "Itineraries(guid\'%s\')";
    static final String PATH_FIND_NEARBY_STOPS = "FindStopsInArea";
    static final String PATH_FIND_NEARBY_ROUTES = "FindRoutesInArea";
    static final String PATH_CALCULATE_ITINERARY_BY_POINTS = "CalculateItineraryByPoints";
    static final String PATH_CALCULATE_ITINERARY_ARRIVING_AT = "CalculateItineraryArrivingAt";
    static final String PATH_FINAL_STOP = "FinalStop";
    static final String PATH_ARRIVALS = "Arrivals";
    static final String PATH_VEHICLES = "Vehicles";
    static final String PATH_LEGS = "Legs";

    static final String PARAM_KEY_FORMAT = "$format";
    static final String PARAM_KEY_AUTH_TOKEN = "subscription-key";
    static final String PARAM_KEY_FILTER = "$filter";
    static final String PARAM_KEY_LAT = "lat";
    static final String PARAM_KEY_LON = "lon";
    static final String PARAM_KEY_LAT1 = "lat1";
    static final String PARAM_KEY_LON1 = "lon1";
    static final String PARAM_KEY_LAT2 = "lat2";
    static final String PARAM_KEY_LON2 = "lon2";
    static final String PARAM_KEY_ORDERBY = "orderby";
    static final String PARAM_KEY_START_TIME = "startTime";
    static final String PARAM_KEY_END_TIME = "endTime";
    static final String PARAM_KEY_EXPAND = "expand";
    static final String PARAM_KEY_RADIUS_MI = "radius"; // in miles

    static final String PARAM_FORMAT = "json";
    static final String PARAM_FILTER_DIRECTION = "DirectionId eq \'%s_%s\'";
    static final String PARAM_FILTER_ROUTE_NAME = "RouteName eq \'%s\'";
    static final String PARAM_FILTER_ROUTE_ID = "RouteId eq \'%s\'";
    static final String PARAM_ORDERBY_ENDTIME = "EndTime";
    static final String PARAM_EXPAND_LEGS = "Legs";

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes" + DEFAULT_PARAMS)
    Observable<List<RouteEntity>> routes();

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{id}\')" + DEFAULT_PARAMS)
    Observable<RouteEntity> route(@Path("id") final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{id}\')/Stops" + DEFAULT_PARAMS)
    Observable<List<StopEntity>> stopsByRoute(@Path("id") final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{routeId}\')/Stops" + DEFAULT_PARAMS)
    Observable<List<StopEntity>> stopsByRouteAndDirection(
            @Path("routeId") final String routeId,
            @Query(PARAM_KEY_FILTER) final String dirId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Vehicles" + DEFAULT_PARAMS)
    Observable<List<VehicleEntity>> vehiclesByRoute(@Query(PARAM_KEY_FILTER) final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Stops(\'{id}\')/Arrivals" + DEFAULT_PARAMS)
    Observable<List<ArrivalEntity>> arrivalsByStop(@Path("id") final String stopId);

    Observable<ItineraryEntity> calculateItinerary(final double lat1, final double lon1,
                                                   final double lat2, final double lon2);

    Observable<ItineraryEntity> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                              final double lat2, final double lon2,
                                                              final String endTime); //TODO String or Date object?

    Observable<ItineraryEntity> itinerary(final String itineraryId);

    Observable<List<IncidentEntity>> getIncidents();
}
