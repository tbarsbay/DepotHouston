package com.tamerbarsbay.depothouston.data.net;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.RouteEntityJsonMapper;

import java.util.List;

import rx.Observable;

/**
 * Created by Tamer on 7/22/2015.
 */
public class RestApiImpl implements RestApi {

    private final Context context;
    private final RouteEntityJsonMapper routeEntityJsonMapper;
    //private final StopEntityJsonMapper stopEntityJsonMapper;


    @Override
    public Observable<RouteEntity> getRouteDetails(String routeId) {
        return null;
    }

    @Override
    public Observable<List<RouteEntity>> getRouteList() {
        return null;
    }

    @Override
    public Observable<List<StopEntity>> getStopsByRoute(String routeId) {
        return null;
    }
}
