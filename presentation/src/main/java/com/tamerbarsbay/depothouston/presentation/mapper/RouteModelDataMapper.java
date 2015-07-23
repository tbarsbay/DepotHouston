package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Route;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
public class RouteModelDataMapper {

    @Inject
    public RouteModelDataMapper() {}

    public RouteModel transform(Route route) {
        if (route == null) {
            throw new IllegalArgumentException("Cannot transform a null value.");
        }
        RouteModel routeModel =  new RouteModel(route.getRouteId());
        routeModel.setFinalStop0Id(route.getFinalStop0Id());
        routeModel.setFinalStop1Id(route.getFinalStop1Id());
        routeModel.setLongName(route.getLongName());
        routeModel.setRouteName(route.getRouteName());
        routeModel.setRouteType(route.getRouteType());
        return routeModel;
    }

    public Collection<RouteModel> transform(Collection<Route> routes) {
        Collection<RouteModel> routeModels;

        if (routes != null && !routes.isEmpty()) {
            routeModels = new ArrayList<RouteModel>();
            for (Route route : routes) {
                routeModels.add(transform(route));
            }
        } else {
            routeModels = Collections.emptyList();
        }

        return routeModels;
    }

}
