package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/22/2015.
 */
public class RouteEntityJsonMapper {

    private final Gson gson;

    @Inject
    public RouteEntityJsonMapper() {
        this.gson = new Gson();
    }

    public RouteEntity transformRouteEntity(String routeJsonResponse) throws JsonSyntaxException {
        try {
            Type routeEntityType = new TypeToken<RouteEntity>() {}.getType();
            RouteEntity routeEntity = this.gson.fromJson(routeJsonResponse, routeEntityType);
            return routeEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<RouteEntity> transformRouteEntityCollection(String routeListJsonResponse) throws JsonSyntaxException {
        List<RouteEntity> routeEntities;
        try {
            Type listOfRouteEntitiesType = new TypeToken<List<RouteEntity>>() {}.getType();
            routeEntities = this.gson.fromJson(routeListJsonResponse, listOfRouteEntitiesType);
            return routeEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
}
