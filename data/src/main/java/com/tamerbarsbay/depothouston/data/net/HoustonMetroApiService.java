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

public interface HoustonMetroApiService {

    String CONTENT_TYPE_JSON_HEADER = "Content-Type: application/json; charset=utf-8";
    String AUTH_TOKEN = "baee3193bf9849f58d0ce02feb3ca7c3"; //TODO remove later
    String DEFAULT_PARAMS = "?$format=json&subscription-key=" + AUTH_TOKEN; //TODO temp

    String PARAM_KEY_FILTER = "$filter";
    String PARAM_KEY_LAT = "lat";
    String PARAM_KEY_LON = "lon";
    String PARAM_KEY_LAT1 = "lat1";
    String PARAM_KEY_LON1 = "lon1";
    String PARAM_KEY_LAT2 = "lat2";
    String PARAM_KEY_LON2 = "lon2";
    String PARAM_KEY_ORDERBY = "orderby";
    String PARAM_KEY_START_TIME = "startTime";
    String PARAM_KEY_END_TIME = "endTime";
    String PARAM_KEY_EXPAND = "expand";
    String PARAM_KEY_RADIUS_MI = "radius"; // in miles

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes" + DEFAULT_PARAMS)
    Observable<List<RouteEntity>> routes();

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("GeoAreas(\'{lat}|{lon}|{radius}\')/Routes" + DEFAULT_PARAMS)
    Observable<List<RouteEntity>> routesNearLocation(@Path("lat") final double lat,
                                                     @Path("lon") final double lon,
                                                     @Path("radius") final String radius);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{id}\')" + DEFAULT_PARAMS)
    Observable<RouteEntity> route(@Path("id") final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{id}\')/Stops" + DEFAULT_PARAMS)
    Observable<List<StopEntity>> stopsByRoute(@Path("id") final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("GeoAreas(\'{lat}|{lon}|{radius}\')/Stops" + DEFAULT_PARAMS)
    Observable<List<StopEntity>> stopsNearLocation(@Path("lat") final double lat,
                                                   @Path("lon") final double lon,
                                                   @Path("radius") final String radiusInMiles);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Routes(\'{routeId}\')/Stops" + DEFAULT_PARAMS)
    Observable<List<StopEntity>> stopsByRouteAndDirection(
            @Path("routeId") final String routeId,
            @Query(PARAM_KEY_FILTER) final String dirId);

    Observable<List<VehicleEntity>> vehiclesByRoute(final String routeId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Vehicles" + DEFAULT_PARAMS)
    Observable<List<VehicleEntity>> vehiclesByRouteFilter(@Query(PARAM_KEY_FILTER) final String filter);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Stops(\'{id}\')/Arrivals" + DEFAULT_PARAMS)
    Observable<List<ArrivalEntity>> arrivalsByStop(@Path("id") final String stopId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("CalculateItineraryByPoints" + DEFAULT_PARAMS)
    Observable<ItineraryEntity> calculateItinerary(
            @Query(PARAM_KEY_LAT1) final double lat1,
            @Query(PARAM_KEY_LON1) final double lon1,
            @Query(PARAM_KEY_LAT2) final double lat2,
            @Query(PARAM_KEY_LON2) final double lon2,
            @Query(PARAM_KEY_ORDERBY) final String orderBy,
            @Query(PARAM_KEY_EXPAND) final String expand);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("CalculateItineraryArrivingAt" + DEFAULT_PARAMS)
    Observable<ItineraryEntity> calculateItineraryWithEndTime(
            @Query(PARAM_KEY_LAT1) final double lat1,
            @Query(PARAM_KEY_LON1) final double lon1,
            @Query(PARAM_KEY_LAT2) final double lat2,
            @Query(PARAM_KEY_LON2) final double lon2,
            @Query(PARAM_KEY_END_TIME) final String endTime,
            @Query(PARAM_KEY_ORDERBY) final String orderBy,
            @Query(PARAM_KEY_EXPAND) final String expand);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Itineraries(guid\'{id}\')" + DEFAULT_PARAMS)
    Observable<ItineraryEntity> itinerary(@Path("id") final String itineraryId);

    @Headers(CONTENT_TYPE_JSON_HEADER)
    @GET("Incidents")
    Observable<List<IncidentEntity>> incidents();
}
