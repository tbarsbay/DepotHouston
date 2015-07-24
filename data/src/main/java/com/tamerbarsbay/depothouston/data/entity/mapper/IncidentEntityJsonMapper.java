package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
public class IncidentEntityJsonMapper {

    private final Gson gson;

    @Inject
    public IncidentEntityJsonMapper() {
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

    public IncidentEntity transformIncidentEntity(String incidentJsonResponse) throws JsonSyntaxException {
        incidentJsonResponse = cleanJson(incidentJsonResponse);
        try {
            Type incidentEntityType = new TypeToken<IncidentEntity>() {}.getType();
            IncidentEntity incidentEntity = this.gson.fromJson(incidentJsonResponse, incidentEntityType);
            return incidentEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<IncidentEntity> transformIncidentEntityCollection(String incidentListJsonResponse) throws JsonSyntaxException {
        incidentListJsonResponse = cleanJson(incidentListJsonResponse);
        List<IncidentEntity> incidentEntities;
        try {
            Type listOfIncidentEntitiesType = new TypeToken<List<IncidentEntity>>() {}.getType();
            incidentEntities = this.gson.fromJson(incidentListJsonResponse, listOfIncidentEntitiesType);
            return incidentEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
}
