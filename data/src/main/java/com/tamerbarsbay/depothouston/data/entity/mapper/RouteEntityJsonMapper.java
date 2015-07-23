package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    /**
     * Get rid of some unneeded outer layers of the returned JSON object.
     * @param jsonResponse
     * @return Cleaned JSON string.
     */
    public String cleanJson(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray results = json.getJSONObject("d").getJSONArray("results");
            return results.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RouteEntity transformRouteEntity(String routeJsonResponse) throws JsonSyntaxException {
        routeJsonResponse = cleanJson(routeJsonResponse);
        try {
            Type routeEntityType = new TypeToken<RouteEntity>() {}.getType();
            RouteEntity routeEntity = this.gson.fromJson(routeJsonResponse, routeEntityType);
            return routeEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<RouteEntity> transformRouteEntityCollection(String routeListJsonResponse) throws JsonSyntaxException {
        routeListJsonResponse = cleanJson(routeListJsonResponse);
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
