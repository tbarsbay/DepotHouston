package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
public class ArrivalModelDataMapper {

    @Inject
    public ArrivalModelDataMapper() {}

    public ArrivalModel transform(Arrival arrival) {
        if (arrival == null) {
            throw new IllegalArgumentException("Cannot transform a null arrival.");
        }
        ArrivalModel arrivalModel = arrivalModel = new ArrivalModel(arrival.getArrivalId());
        arrivalModel.setStopId(arrival.getStopId());
        arrivalModel.setDelaySeconds(arrival.getDelaySeconds());
        arrivalModel.setDestinationName(arrival.getDestinationName());
        arrivalModel.setDestinationStopId(arrival.getDestinationStopId());
        arrivalModel.setIsRealTime(arrival.isRealTime());
        arrivalModel.setLocalArrivalTime(arrival.getLocalArrivalTime());
        arrivalModel.setLocalDepartureTime(arrival.getLocalDepartureTime());
        arrivalModel.setRouteId(arrival.getRouteId());
        arrivalModel.setRouteName(arrival.getRouteName());
        arrivalModel.setRouteType(arrival.getRouteType());
        arrivalModel.setStopName(arrival.getStopName());
        arrivalModel.setStopSequence(arrival.getStopSequence());
        arrivalModel.setUtcArrivalTime(arrival.getUtcArrivalTime());
        arrivalModel.setUtcDepartureTime(arrival.getUtcDepartureTime());
        return arrivalModel;
    }

    public Collection<ArrivalModel> transform(Collection<Arrival> arrivals) {
        Collection<ArrivalModel> arrivalModels;

        if (arrivals != null && !arrivals.isEmpty()) {
            arrivalModels = new ArrayList<ArrivalModel>();
            for (Arrival arrival : arrivals) {
                arrivalModels.add(transform(arrival));
            }
        } else {
            arrivalModels = Collections.emptyList();
        }

        return arrivalModels;
    }

}
