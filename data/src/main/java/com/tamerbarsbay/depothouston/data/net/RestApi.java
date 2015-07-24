package com.tamerbarsbay.depothouston.data.net;

import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;

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
    static final String PARAM_AUTH_TOKEN = "baee3193bf9849f58d0ce02feb3ca7c3"; //TODO remove later
    static final String PARAM_FILTER_DIRECTION = "DirectionId eq \'%s_%s\'";
    static final String PARAM_FILTER_ROUTE_NAME = "RouteName eq \'%s\'";
    static final String PARAM_FILTER_ROUTE_ID = "RouteId eq \'%s\'";
    static final String PARAM_ORDERBY_ENDTIME = "EndTime";
    static final String PARAM_EXPAND_LEGS = "Legs";

    Observable<List<RouteEntity>> getRouteList();

    Observable<RouteEntity> getRouteDetails(final String routeId);

    Observable<List<StopEntity>> getStopsByRoute(final String routeId);

    Observable<List<VehicleEntity>> getVehiclesByRoute(final String routeId);

    Observable<List<ArrivalEntity>> getArrivalsByStop(final String stopId);

    Observable<ItineraryEntity> calculateItinerary(final double lat1, final double lon1,
                                                   final double lat2, final double lon2);

    Observable<ItineraryEntity> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                              final double lat2, final double lon2,
                                                              final String endTime); //TODO String or Date object?

    Observable<ItineraryEntity> getItineraryDetails(final String itineraryId);

    Observable<List<IncidentEntity>> getIncidents();
}
