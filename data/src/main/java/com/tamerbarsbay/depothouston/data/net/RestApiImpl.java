package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.squareup.okhttp.OkHttpClient;
import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.IncidentEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.ItineraryEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.exception.NetworkConnectionException;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscriber;

public class RestApiImpl implements RestApi {

    private final Context context;
    private IncidentEntityJsonMapper incidentEntityJsonMapper;
    private ItineraryEntityJsonMapper itineraryEntityJsonMapper;

    private static OkHttpClient okHttpClient;

    public RestApiImpl(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
        this.context = context.getApplicationContext();
    }

    public RestApiImpl(Context context, IncidentEntityJsonMapper incidentEntityJsonMapper) {
        if (context == null || incidentEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.incidentEntityJsonMapper = incidentEntityJsonMapper;
    }

    public RestApiImpl(Context context, ItineraryEntityJsonMapper itineraryEntityJsonMapper) {
        if (context == null || itineraryEntityJsonMapper == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null.");
        }
        this.context = context.getApplicationContext();
        this.itineraryEntityJsonMapper = itineraryEntityJsonMapper;
    }

    @Override
    public Observable<RouteEntity> route(final String routeId) {
        if (validNetworkConnection()) {
            return getRetrofit().create(RestApi.class).route(routeId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    @Override
    public Observable<List<RouteEntity>> routes() {
        if (validNetworkConnection()) {
            return getRetrofit().create(RestApi.class).routes();
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    @Override
    public Observable<List<StopEntity>> stopsByRoute(final String routeId) {
        return stopsByRouteAndDirection(routeId, "0"); //TODO temp
    }

    @Override
    public Observable<List<StopEntity>> stopsByRouteAndDirection(String routeId, String dirId) {
        if (validNetworkConnection()) {
            dirId = String.format(PARAM_FILTER_DIRECTION, routeId, dirId);
            return getRetrofit().create(RestApi.class).stopsByRouteAndDirection(routeId, dirId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    @Override
    public Observable<List<ArrivalEntity>> arrivalsByStop(final String stopId) {
        if (validNetworkConnection()) {
            return getRetrofit().create(RestApi.class).arrivalsByStop(stopId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    @Override
    public Observable<List<VehicleEntity>> vehiclesByRoute(final String routeId) {
        if (validNetworkConnection()) {
            String routeFilter = String.format(PARAM_FILTER_ROUTE_ID, routeId);
            return getRetrofit().create(RestApi.class).vehiclesByRoute(routeFilter);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
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
    public Observable<ItineraryEntity> itinerary(final String itineraryId) {
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
                .appendQueryParameter(PARAM_KEY_AUTH_TOKEN, AUTH_TOKEN);
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

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(CustomGsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
            okHttpClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
            okHttpClient.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        }
        return okHttpClient;
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
