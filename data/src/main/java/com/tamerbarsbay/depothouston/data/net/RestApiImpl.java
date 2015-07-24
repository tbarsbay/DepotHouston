package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.ArrivalEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.IncidentEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.ItineraryEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.RouteEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.StopEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.VehicleEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.exception.NetworkConnectionException;

import java.net.MalformedURLException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Tamer on 7/22/2015.
 */
public class RestApiImpl implements RestApi {

    private final Context context;
    private RouteEntityJsonMapper routeEntityJsonMapper;
    private StopEntityJsonMapper stopEntityJsonMapper;
    private ArrivalEntityJsonMapper arrivalEntityJsonMapper;
    private IncidentEntityJsonMapper incidentEntityJsonMapper;
    private VehicleEntityJsonMapper vehicleEntityJsonMapper;
    private ItineraryEntityJsonMapper itineraryEntityJsonMapper;

    public RestApiImpl(Context context, RouteEntityJsonMapper routeEntityJsonMapper) {
        if (context == null || routeEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.routeEntityJsonMapper = routeEntityJsonMapper;
    }

    public RestApiImpl(Context context, StopEntityJsonMapper stopEntityJsonMapper) {
        if (context == null || stopEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.stopEntityJsonMapper = stopEntityJsonMapper;
    }

    public RestApiImpl(Context context, ArrivalEntityJsonMapper arrivalEntityJsonMapper) {
        if (context == null || arrivalEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.arrivalEntityJsonMapper = arrivalEntityJsonMapper;
    }

    public RestApiImpl(Context context, IncidentEntityJsonMapper incidentEntityJsonMapper) {
        if (context == null || incidentEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.incidentEntityJsonMapper = incidentEntityJsonMapper;
    }

    public RestApiImpl(Context context, VehicleEntityJsonMapper vehicleEntityJsonMapper) {
        if (context == null || vehicleEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.vehicleEntityJsonMapper = vehicleEntityJsonMapper;
    }

    public RestApiImpl(Context context, ItineraryEntityJsonMapper itineraryEntityJsonMapper) {
        if (context == null || itineraryEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.itineraryEntityJsonMapper = itineraryEntityJsonMapper;
    }

    @Override
    public Observable<RouteEntity> getRouteDetails(final String routeId) {
        return Observable.create(new Observable.OnSubscribe<RouteEntity>() {
            @Override
            public void call(Subscriber<? super RouteEntity> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseRouteEntity = getRouteDetailsFromApi(routeId);
                        if (responseRouteEntity != null) {
                            subscriber.onNext(routeEntityJsonMapper.transformRouteEntity(
                                    responseRouteEntity));
                        } else {
                            subscriber.onError(new Exception("Null route entity.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<RouteEntity>> getRouteList() {
        return Observable.create(new Observable.OnSubscribe<List<RouteEntity>>() {
            @Override
            public void call(Subscriber<? super List<RouteEntity>> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseRouteEntities = getRouteListFromApi();
                        if (responseRouteEntities != null) {
                            subscriber.onNext(routeEntityJsonMapper.transformRouteEntityCollection(
                                    responseRouteEntities));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null route entities.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e); //TODO example has NetworkConnectionException here
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<StopEntity>> getStopsByRoute(final String routeId) {
        return Observable.create(new Observable.OnSubscribe<List<StopEntity>>() {
            @Override
            public void call(Subscriber<? super List<StopEntity>> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseStopEntities = getStopsByRouteAndDirectionFromApi(routeId, "0"); //TODO temp
                        if (responseStopEntities != null) {
                            subscriber.onNext(stopEntityJsonMapper.transformStopEntityCollection(
                                    responseStopEntities));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null stop entities.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<ArrivalEntity>> getArrivalsByStop(final String stopId) {
        return Observable.create(new Observable.OnSubscribe<List<ArrivalEntity>>() {
            @Override
            public void call(Subscriber<? super List<ArrivalEntity>> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseArrivalEntities = getArrivalsByStopFromApi(stopId);
                        if (responseArrivalEntities != null) {
                            subscriber.onNext(
                                    arrivalEntityJsonMapper.transformArrivalEntityCollection(
                                            responseArrivalEntities));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null arrival entities.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<VehicleEntity>> getVehiclesByRoute(final String routeId) {
        return Observable.create(new Observable.OnSubscribe<List<VehicleEntity>>() {
            @Override
            public void call(Subscriber<? super List<VehicleEntity>> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseVehicleEntities = getVehiclesByRouteFromApi(routeId);
                        if (responseVehicleEntities != null) {
                            subscriber.onNext(
                                    vehicleEntityJsonMapper.transformVehicleEntityCollection(
                                            responseVehicleEntities));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null vehicle entities.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<ItineraryEntity> calculateItinerary(final double lat1, final double lon1,
                                                          final double lat2, final double lon2) {
        return Observable.create(new Observable.OnSubscribe<ItineraryEntity>() {
            @Override
            public void call(Subscriber<? super ItineraryEntity> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseItineraryEntity = calculateItineraryFromApi(
                                lat1, lon1, lat2, lon2);
                        if (responseItineraryEntity != null) {
                            subscriber.onNext(itineraryEntityJsonMapper.transformItineraryEntity(
                                    responseItineraryEntity));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null itinerary entity.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<ItineraryEntity> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                                     final double lat2, final double lon2,
                                                                     final String endTime) {
        return Observable.create(new Observable.OnSubscribe<ItineraryEntity>() {
            @Override
            public void call(Subscriber<? super ItineraryEntity> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseItineraryEntity = calculateItineraryWithEndTimeFromApi(
                                lat1, lon1, lat2, lon2, endTime);
                        if (responseItineraryEntity != null) {
                            subscriber.onNext(itineraryEntityJsonMapper.transformItineraryEntity(
                                    responseItineraryEntity));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null itinerary entity.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<ItineraryEntity> getItineraryDetails(final String itineraryId) {
        return Observable.create(new Observable.OnSubscribe<ItineraryEntity>() {
            @Override
            public void call(Subscriber<? super ItineraryEntity> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseItineraryEntity = getItineraryDetailsFromApi(itineraryId);
                        if (responseItineraryEntity != null) {
                            subscriber.onNext(itineraryEntityJsonMapper
                                    .transformItineraryEntity(responseItineraryEntity));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null itinerary entity.")); //TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    @Override
    public Observable<List<IncidentEntity>> getIncidents() {
        return Observable.create(new Observable.OnSubscribe<List<IncidentEntity>>() {
            @Override
            public void call(Subscriber<? super List<IncidentEntity>> subscriber) {
                if (validNetworkConnection()) {
                    try {
                        String responseIncidentEntities = getIncidentsFromApi();
                        if (responseIncidentEntities != null) {
                            subscriber.onNext(incidentEntityJsonMapper
                                .transformIncidentEntityCollection(responseIncidentEntities));
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Exception("Null incident entities."));//TODO temp
                        }
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                } else {
                    subscriber.onError(new NetworkConnectionException());
                }
            }
        });
    }

    private String getRouteListFromApi() throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(PATH_ROUTES);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getRouteDetailsFromApi(String routeId) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_ROUTE_WITH_ID, routeId));
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getStopListByRoute(String routeId) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_ROUTE_WITH_ID, routeId))
                .appendPath(PATH_STOPS);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getStopsByRouteAndDirectionFromApi(String routeId, String dir) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_ROUTE_WITH_ID, routeId))
                .appendPath(PATH_STOPS)
                .appendQueryParameter(PARAM_KEY_FILTER, String.format(PARAM_FILTER_DIRECTION, routeId, dir));
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getArrivalsByStopFromApi(String stopId) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_STOP_WITH_ID, stopId))
                .appendPath(PATH_ARRIVALS);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getVehiclesByRouteFromApi(String routeId) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(PATH_VEHICLES)
                .appendQueryParameter(PARAM_KEY_FILTER, String.format(PARAM_FILTER_ROUTE_ID, routeId));
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String calculateItineraryFromApi(double lat1, double lon1,
                                            double lat2, double lon2) throws MalformedURLException{
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(PATH_CALCULATE_ITINERARY_BY_POINTS)
                .appendQueryParameter(PARAM_KEY_LAT1, String.valueOf(lat1))
                .appendQueryParameter(PARAM_KEY_LON1, String.valueOf(lon1))
                .appendQueryParameter(PARAM_KEY_LAT2, String.valueOf(lat2))
                .appendQueryParameter(PARAM_KEY_LON2, String.valueOf(lon2))
                .appendQueryParameter(PARAM_KEY_ORDERBY, PARAM_ORDERBY_ENDTIME)
                .appendQueryParameter(PARAM_KEY_EXPAND, PARAM_EXPAND_LEGS);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String calculateItineraryWithEndTimeFromApi(double lat1, double lon1,
                                                       double lat2, double lon2,
                                                       String endTime) throws MalformedURLException{
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(PATH_CALCULATE_ITINERARY_ARRIVING_AT)
                .appendQueryParameter(PARAM_KEY_LAT1, String.valueOf(lat1))
                .appendQueryParameter(PARAM_KEY_LON1, String.valueOf(lon1))
                .appendQueryParameter(PARAM_KEY_LAT2, String.valueOf(lat2))
                .appendQueryParameter(PARAM_KEY_LON2, String.valueOf(lon2))
                .appendQueryParameter(PARAM_KEY_ORDERBY, PARAM_ORDERBY_ENDTIME)
                .appendQueryParameter(PARAM_KEY_END_TIME, endTime)
                .appendQueryParameter(PARAM_KEY_EXPAND, PARAM_EXPAND_LEGS);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getItineraryDetailsFromApi(String itineraryId) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_ITINERARY_WITH_ID, itineraryId));
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private String getIncidentsFromApi() throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(PATH_INCIDENTS);
        return ApiConnection.createGET(builder.build().toString()).requestSyncCall();
    }

    private Uri.Builder getBaseUriBuilderMetro() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_HTTPS)
                .authority(AUTHORITY_METRO)
                .appendPath(PATH_DATA)
                .appendQueryParameter(PARAM_KEY_FORMAT, PARAM_FORMAT)
                .appendQueryParameter(PARAM_KEY_AUTH_TOKEN, PARAM_AUTH_TOKEN);
        return builder;
    }

    private Uri.Builder getBaseUriBuilderCommuter() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME_HTTPS)
                .authority(AUTHORITY_COMMUTER)
                .appendPath(PATH_TRANSIT_SERVICE)
                .appendQueryParameter(PARAM_KEY_FORMAT, PARAM_FORMAT);
        return builder;
    }

    private boolean validNetworkConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
