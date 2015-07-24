package com.tamerbarsbay.depothouston.data.cache.serializer;

import com.google.gson.Gson;
import com.tamerbarsbay.depothouston.data.entity.ItineraryEntity;
import com.tamerbarsbay.depothouston.data.entity.RouteEntity;
import com.tamerbarsbay.depothouston.data.entity.StopEntity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/24/2015.
 */
@Singleton
public class JsonSerializer {

    private final Gson gson = new Gson();

    @Inject
    public JsonSerializer() {}

    /**
     * Serialize a Route object to Json.
     *
     * @param routeEntity {@link RouteEntity} to serialize.
     */
    public String serialize(RouteEntity routeEntity) {
        String jsonString = gson.toJson(routeEntity, RouteEntity.class);
        return jsonString;
    }

    /**
     * Serialize a Stop object to Json.
     *
     * @param stopEntity {@link StopEntity} to serialize.
     */
    public String serialize(StopEntity stopEntity) {
        String jsonString = gson.toJson(stopEntity, StopEntity.class);
        return jsonString;
    }

    /**
     * Serialize an Itinerary object to Json.
     *
     * @param itineraryEntity {@link ItineraryEntity} to serialize.
     */
    public String serialize(ItineraryEntity itineraryEntity) {
        String jsonString = gson.toJson(itineraryEntity, ItineraryEntity.class);
        return jsonString;
    }

    /**
     * Deserialize a json representation of a Route object.
     *
     * @param jsonString A json string to deserialize.
     * @return {@link RouteEntity}
     */
    public RouteEntity deserializeRoute(String jsonString) {
        RouteEntity routeEntity = gson.fromJson(jsonString, RouteEntity.class);
        return routeEntity;
    }

    /**
     * Deserialize a json representation of a Stop object.
     *
     * @param jsonString A json string to deserialize.
     * @return {@link StopEntity}
     */
    public StopEntity deserializeStop(String jsonString) {
        StopEntity stopEntity = gson.fromJson(jsonString, StopEntity.class);
        return stopEntity;
    }

    /**
     * Deserialize a json representation of an Itinerary object.
     *
     * @param jsonString A json string to deserialize.
     * @return {@link ItineraryEntity}
     */
    public ItineraryEntity deserializeItinerary(String jsonString) {
        ItineraryEntity itineraryEntity = gson.fromJson(jsonString, ItineraryEntity.class);
        return itineraryEntity;
    }
}