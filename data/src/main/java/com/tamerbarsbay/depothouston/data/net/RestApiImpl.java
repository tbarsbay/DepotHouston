package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
                        String responseStopEntities = getStopListByRoute(routeId);
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
        return ApiConnection.createGET(RestApi.API_URL_GET_ROUTE_LIST).requestSyncCall();
    }

    private String getRouteDetailsFromApi(String routeId) throws MalformedURLException {
        String apiUrl = String.format(RestApi.API_URL_GET_ROUTE_DETAILS, routeId) +
                "?" + JSON_FORMAT_PARAM;
        return ApiConnection.createGET(apiUrl).requestSyncCall();
    }

    private String getStopListByRoute(String routeId) throws MalformedURLException {
        String apiUrl = String.format(RestApi.API_URL_GET_STOPS_BY_ROUTE, routeId) +
                "?" + JSON_FORMAT_PARAM;
        return ApiConnection.createGET(apiUrl).requestSyncCall();
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
