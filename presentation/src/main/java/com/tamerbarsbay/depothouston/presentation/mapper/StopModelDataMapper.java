package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
public class StopModelDataMapper {

    @Inject
    public StopModelDataMapper() {}

    public StopModel transform(Stop stop) {
        if (stop == null) {
            throw new IllegalArgumentException("Cannot transform a null value.");
        }
        StopModel stopModel =  new StopModel(stop.getId());
        stopModel.setType(stop.getType());
        stopModel.setAgencyId(stop.getAgencyId());
        stopModel.setDirectionId(stop.getDirectionId());
        stopModel.setLat(stop.getLat());
        stopModel.setLon(stop.getLon());
        stopModel.setName(stop.getName());
        stopModel.setOrdinal(stop.getOrdinal());
        stopModel.setOrdinalOnDirection(stop.getOrdinalOnDirection());
        stopModel.setRouteId(stop.getRouteId());
        stopModel.setStopCode(stop.getStopCode());
        stopModel.setStopId(stop.getStopId());
        return stopModel;
    }

    public Collection<StopModel> transform(Collection<Stop> stops) {
        Collection<StopModel> stopModels;

        if (stops != null && !stops.isEmpty()) {
            stopModels = new ArrayList<StopModel>();
            for (Stop stop : stops) {
                stopModels.add(transform(stop));
            }
        } else {
            stopModels = Collections.emptyList();
        }

        return stopModels;
    }
}
