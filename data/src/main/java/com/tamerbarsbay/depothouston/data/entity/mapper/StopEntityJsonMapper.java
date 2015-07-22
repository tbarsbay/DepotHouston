package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/22/2015.
 */
public class StopEntityJsonMapper {

    private final Gson gson;

    @Inject
    public StopEntityJsonMapper() {
        this.gson = new Gson();
    }

    public StopEntity transformStopEntity(String stopJsonResponse) throws JsonSyntaxException {
        try {
            Type stopEntityType = new TypeToken<StopEntity>() {}.getType();
            StopEntity stopEntity = this.gson.fromJson(stopJsonResponse, stopEntityType);
            return stopEntity;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }

    public List<StopEntity> transformStopEntityCollection(String stopListJsonResponse) throws JsonSyntaxException {
        List<StopEntity> stopEntities;
        try {
            Type listOfStopEntitiesType = new TypeToken<List<StopEntity>>() {}.getType();
            stopEntities = this.gson.fromJson(stopListJsonResponse, listOfStopEntitiesType);
            return stopEntities;
        } catch (JsonSyntaxException exception) {
            throw exception;
        }
    }
}
