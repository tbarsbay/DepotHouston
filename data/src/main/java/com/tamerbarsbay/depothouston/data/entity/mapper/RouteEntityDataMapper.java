package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.domain.Route;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/22/2015.
 */
@Singleton
public class RouteEntityDataMapper {

    @Inject
    public RouteEntityDataMapper() {}

    public Route transform(RouteEntity routeEntity) {
        Route route = null;
        if (routeEntity != null) {
            route = new Route(routeEntity.getRouteId());
            route.setFinalStop0Id(routeEntity.getFinalStop0Id());
            route.setFinalStop1Id(routeEntity.getFinalStop1Id());
            route.setLongName(routeEntity.getLongName());
            route.setRouteName(routeEntity.getRouteName());
            route.setRouteType(routeEntity.getRouteType());
        }
        return route;
    }

    public List<Route> transform(List<RouteEntity> routeEntities) {
        List<Route> routes = new ArrayList<Route>();
        Route route;
        for (RouteEntity routeEntity : routeEntities) {
            route = transform(routeEntity);
            if (route != null) {
                routes.add(route);
            }
        }
        return routes;
    }

}
