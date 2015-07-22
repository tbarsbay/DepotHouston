package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.StopEntity;
import com.tamerbarsbay.depothouston.domain.Stop;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/22/2015.
 */
@Singleton
public class StopEntityDataMapper {

    @Inject
    public StopEntityDataMapper() {}

    public Stop transform(StopEntity stopEntity) {
        Stop stop = null;
        if (stopEntity != null) {
            stop =  new Stop(stopEntity.getId());
            stop.setAgencyId(stopEntity.getAgencyId());
            stop.setDirectionId(stopEntity.getDirectionId());
            stop.setLat(stopEntity.getLat());
            stop.setLon(stopEntity.getLon());
            stop.setName(stopEntity.getName());
            stop.setOrdinal(stopEntity.getOrdinal());
            stop.setOrdinalOnDirection(stopEntity.getOrdinalOnDirection());
            stop.setRouteId(stopEntity.getRouteId());
            stop.setStopCode(stopEntity.getStopCode());
            stop.setStopId(stopEntity.getStopId());
            stop.setType(stopEntity.getType());
        }
        return stop;
    }

    public List<Stop> transform(List<StopEntity> stopEntities) {
        List<Stop> stops = new ArrayList<Stop>();
        Stop stop;
        for (StopEntity stopEntity : stopEntities) {
            stop = transform(stopEntity);
            if (stop != null) {
                stops.add(stop);
            }
        }
        return stops;
    }

}
