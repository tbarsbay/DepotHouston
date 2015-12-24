package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;
import com.tamerbarsbay.depothouston.data.exception.NetworkConnectionException;
import com.tamerbarsbay.depothouston.data.util.DistanceUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.functions.Func1;

public class HoustonMetroApi {

    private final Context context;

    private static OkHttpClient okHttpClient;

    private static final String RIDEMETRO_API_BASE_URL = "https://api.ridemetro.org/data/";
    private static final String COMMUTER_API_BASE_URL = "https://services.commuterapi.com/TransitODataService.svc";
    private static final String PARAM_FILTER_DIRECTION = "DirectionId eq \'%s_%s\'";
    private static final String PARAM_FILTER_ROUTE_ID = "RouteId eq \'%s\'";
    private static final String PARAM_ORDERBY_ENDTIME = "EndTime";
    private static final String PARAM_EXPAND_LEGS = "Legs";

    public HoustonMetroApi(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null.");
        }
        this.context = context.getApplicationContext();
    }

    public Observable<RouteEntity> route(final String routeId) {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).route(routeId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<RouteEntity>> routes() {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).routes();
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<RouteEntity>> routesNearLocation(final double lat,
                                                            final double lon,
                                                            final String radiusInMiles) {
        if (validNetworkConnection()) {
            return getRetrofit()
                    .create(HoustonMetroApiService.class)
                    .routesNearLocation(lat, lon, radiusInMiles);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<StopEntity>> stopsByRoute(final String routeId) {
        Log.d("RestApi", "Stops for route: " + routeId); //TODO temp
        return stopsByRouteAndDirection(routeId, "0"); //TODO temp
    }

    public Observable<List<StopEntity>> stopsByRouteAndDirection(String routeId, String dirId) {
        if (validNetworkConnection()) {
            String dirFilter = String.format(PARAM_FILTER_DIRECTION, routeId, dirId);
            return getRetrofit().create(HoustonMetroApiService.class).stopsByRouteAndDirection(routeId, dirFilter);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<StopEntity>> stopsNearLocation(double lat, double lon, String radius) {
        Log.d("RestApi", "Stops within " + radius + " miles of: " + lat + "," + lon); //TODO temp
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).stopsNearLocation(lat, lon, radius);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<StopEntity>> stopsNearLocationByRoute(String routeId,
                                                                 final double lat,
                                                                 final double lon,
                                                                 final String radius) {
        if (validNetworkConnection()) {
            return getRetrofit()
                    .create(HoustonMetroApiService.class).stopsByRoute(routeId)
                    .flatMap(new Func1<List<StopEntity>, Observable<StopEntity>>() {
                        @Override
                        public Observable<StopEntity> call(final List<StopEntity> stopEntities) {
                            return Observable.from(stopEntities).filter(new Func1<StopEntity, Boolean>() {
                                @Override
                                public Boolean call(StopEntity stopEntity) {
                                    double threshold = .25;//TODO make default constant
                                    try {
                                        threshold = Double.parseDouble(radius);
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                    double distance =
                                            DistanceUtils.calculateDistanceBetweenCoordinatesInMiles(
                                                    lat,
                                                    lon,
                                                    stopEntity.getLat(),
                                                    stopEntity.getLon());
                                    //TODO temp logs
                                    Log.d("HoustonMetroApi", "Between: (" + lat + "," + lon + ") and (" + stopEntity.getLat() + "," + stopEntity.getLon() + ")");
                                    Log.d("HoustonMetroApi", "Threshold: " + threshold);
                                    Log.d("HoustonMetroApi", "Distance: " + distance);
                                    return distance < threshold;
                                }
                            });
                        }
                    })
                    .toList();
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<ArrivalEntity>> arrivalsByStop(final String stopId) {
        Log.d("RestApi", "Arrivals for stop: " + stopId); //TODO temp
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).arrivalsByStop(stopId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<VehicleEntity>> vehiclesByRoute(final String routeId) {
        return vehiclesByRouteFilter(String.format(PARAM_FILTER_ROUTE_ID, routeId));
    }

    public Observable<List<VehicleEntity>> vehiclesByRouteFilter(String filter) {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).vehiclesByRouteFilter(filter);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<ItineraryEntity> calculateItinerary(final double lat1, final double lon1,
                                                          final double lat2, final double lon2) {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).calculateItinerary(lat1, lon1,
                    lat2, lon2, PARAM_ORDERBY_ENDTIME, PARAM_EXPAND_LEGS);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<ItineraryEntity> calculateItineraryWithEndTime(final double lat1, final double lon1,
                                                                     final double lat2, final double lon2,
                                                                     final String endTime) {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).calculateItineraryWithEndTime(
                    lat1, lon1, lat2, lon2, endTime, PARAM_ORDERBY_ENDTIME, PARAM_EXPAND_LEGS);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<ItineraryEntity> itinerary(final String itineraryId) {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).itinerary(itineraryId);
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    public Observable<List<IncidentEntity>> incidents() {
        if (validNetworkConnection()) {
            return getRetrofit().create(HoustonMetroApiService.class).incidents();
        } else {
            return Observable.error(new NetworkConnectionException());
        }
    }

    private Uri.Builder getBaseUriBuilderCommuter() {
        //TODO
        return null;
//        Uri.Builder builder = new Uri.Builder();
//        builder.scheme(SCHEME_HTTPS)
//                .authority(AUTHORITY_COMMUTER)
//                .appendPath(PATH_TRANSIT_SERVICE)
//                .appendQueryParameter(PARAM_KEY_FORMAT, PARAM_FORMAT);
//        return builder;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(RIDEMETRO_API_BASE_URL)
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
            //okHttpClient.networkInterceptors().add(new LoggingInterceptor()); //TODO temp
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
