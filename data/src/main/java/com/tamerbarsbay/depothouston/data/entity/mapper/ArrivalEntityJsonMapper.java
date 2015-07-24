package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ArrivalEntityJsonMapper {

    private final Gson gson;

    @Inject
    public ArrivalEntityJsonMapper() {
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

    public ArrivalEntity transformArrivalEntity(String arrivalJsonResponse) throws JsonSyntaxException {
        arrivalJsonResponse = cleanJson(arrivalJsonResponse);
        try {
            Type arrivalEntityType = new TypeToken<ArrivalEntity>() {}.getType();
            ArrivalEntity arrivalEntity = this.gson.fromJson(arrivalJsonResponse, arrivalEntityType);
            return arrivalEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<ArrivalEntity> transformArrivalEntityCollection(String arrivalListJsonResponse) throws JsonSyntaxException {
        arrivalListJsonResponse = cleanJson(arrivalListJsonResponse);
        List<ArrivalEntity> arrivalEntities;
        try {
            Type listOfArrivalEntitiesType = new TypeToken<List<ArrivalEntity>>() {}.getType();
            arrivalEntities = this.gson.fromJson(arrivalListJsonResponse, listOfArrivalEntitiesType);
            return arrivalEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
}
