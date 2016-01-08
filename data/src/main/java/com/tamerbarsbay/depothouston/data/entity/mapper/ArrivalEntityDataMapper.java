package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.ArrivalEntity;
import com.tamerbarsbay.depothouston.domain.Arrival;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ArrivalEntityDataMapper {

    @Inject
    public ArrivalEntityDataMapper() {}

    public Arrival transform(ArrivalEntity arrivalEntity) {
        Arrival arrival = null;
        if (arrivalEntity != null) {
            arrival = new Arrival(arrivalEntity.getArrivalId());
            arrival.setStopId(arrivalEntity.getStopId());
            arrival.setDelaySeconds(arrivalEntity.getDelaySeconds());
            arrival.setDestinationName(arrivalEntity.getDestinationName());
            arrival.setDestinationStopId(arrivalEntity.getDestinationStopId());
            arrival.setIsRealTime(arrivalEntity.isRealTime());
            arrival.setLocalArrivalTime(arrivalEntity.getLocalArrivalTime());
            arrival.setLocalDepartureTime(arrivalEntity.getLocalDepartureTime());
            arrival.setRouteId(arrivalEntity.getRouteId());
            arrival.setRouteName(arrivalEntity.getRouteName());
            arrival.setRouteType(arrivalEntity.getRouteType());
            arrival.setStopName(arrivalEntity.getStopName());
            arrival.setStopSequence(arrivalEntity.getStopSequence());
            arrival.setUtcArrivalTime(arrivalEntity.getUtcArrivalTime());
            arrival.setUtcDepartureTime(arrivalEntity.getUtcDepartureTime());
        }
        return arrival;
    }

    public List<Arrival> transform(Collection<ArrivalEntity> arrivalEntities) {
        List<Arrival> arrivals = new ArrayList<Arrival>();
        Arrival arrival;
        for (ArrivalEntity arrivalEntity : arrivalEntities) {
            arrival = transform(arrivalEntity);
            if (arrival != null) {
                arrivals.add(arrival);
            }
        }
        return arrivals;
    }

}
