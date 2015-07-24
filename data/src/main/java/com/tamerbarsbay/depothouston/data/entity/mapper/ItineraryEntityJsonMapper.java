package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
public class ItineraryEntityJsonMapper {

    private final Gson gson;

    @Inject
    public ItineraryEntityJsonMapper() {
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

    public ItineraryEntity transformItineraryEntity(String itineraryJsonResponse) throws JsonSyntaxException {
        itineraryJsonResponse = cleanJson(itineraryJsonResponse);
        try {
            Type itineraryEntityType = new TypeToken<ItineraryEntity>() {}.getType();
            ItineraryEntity itineraryEntity = this.gson.fromJson(itineraryJsonResponse, itineraryEntityType);
            return itineraryEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<ItineraryEntity> transformItineraryEntityCollection(String itineraryListJsonResponse) throws JsonSyntaxException {
        itineraryListJsonResponse = cleanJson(itineraryListJsonResponse);
        List<ItineraryEntity> itineraryEntities;
        try {
            Type listOfItineraryEntitiesType = new TypeToken<List<ItineraryEntity>>() {}.getType();
            itineraryEntities = this.gson.fromJson(itineraryListJsonResponse, listOfItineraryEntitiesType);
            return itineraryEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
}
