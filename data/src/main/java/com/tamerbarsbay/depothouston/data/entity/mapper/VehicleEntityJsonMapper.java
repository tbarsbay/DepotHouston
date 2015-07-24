package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
public class VehicleEntityJsonMapper {
    
    private final Gson gson;
    
    @Inject
    public VehicleEntityJsonMapper() {
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

    public VehicleEntity transformVehicleEntity(String vehicleJsonResponse) throws JsonSyntaxException {
        vehicleJsonResponse = cleanJson(vehicleJsonResponse);
        try {
            Type vehicleEntityType = new TypeToken<VehicleEntity>() {}.getType();
            VehicleEntity vehicleEntity = this.gson.fromJson(vehicleJsonResponse, vehicleEntityType);
            return vehicleEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<VehicleEntity> transformVehicleEntityCollection(String vehicleListJsonResponse) throws JsonSyntaxException {
        vehicleListJsonResponse = cleanJson(vehicleListJsonResponse);
        List<VehicleEntity> vehicleEntities;
        try {
            Type listOfVehicleEntitiesType = new TypeToken<List<VehicleEntity>>() {}.getType();
            vehicleEntities = this.gson.fromJson(vehicleListJsonResponse, listOfVehicleEntitiesType);
            return vehicleEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
    
}
