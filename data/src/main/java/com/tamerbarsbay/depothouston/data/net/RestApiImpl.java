package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.RouteEntityJsonMapper;
import com.tamerbarsbay.depothouston.data.entity.mapper.StopEntityJsonMapper;
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
                        String responseStopEntities = getStopListByRouteAndDirection(routeId, "0"); //TODO temp
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

    private String getStopListByRouteAndDirection(String routeId, String dir) throws MalformedURLException {
        Uri.Builder builder = getBaseUriBuilderMetro();
        builder.appendPath(String.format(PATH_ROUTE_WITH_ID, routeId))
                .appendPath(PATH_STOPS)
                .appendQueryParameter(PARAM_KEY_FILTER, String.format(PARAM_FILTER_DIRECTION, routeId, dir));
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
