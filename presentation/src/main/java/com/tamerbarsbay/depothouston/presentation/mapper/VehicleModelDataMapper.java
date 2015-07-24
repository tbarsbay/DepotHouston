package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Vehicle;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.VehicleModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
public class VehicleModelDataMapper {

    @Inject
    public VehicleModelDataMapper() {}

    public VehicleModel transform(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Cannot transform a null vehicle.");
        }
        VehicleModel vehicleModel = new VehicleModel(vehicle.getVehicleId());
        vehicleModel.setRouteName(vehicle.getRouteName());
        vehicleModel.setBlock(vehicle.getBlock());
        vehicleModel.setDelaySeconds(vehicle.getDelaySeconds());
        vehicleModel.setDestinationName(vehicle.getDestinationName());
        vehicleModel.setDirectionName(vehicle.getDirectionName());
        vehicleModel.setIsMonitored(vehicle.isMonitored());
        vehicleModel.setLat(vehicle.getLat());
        vehicleModel.setLon(vehicle.getLon());
        vehicleModel.setRouteId(vehicle.getRouteId());
        vehicleModel.setVehicleReportTime(vehicle.getVehicleReportTime());
        return vehicleModel;
    }

    public Collection<VehicleModel> transform(Collection<Vehicle> vehicles) {
        Collection<VehicleModel> vehicleModels;

        if (vehicles != null && !vehicles.isEmpty()) {
            vehicleModels = new ArrayList<VehicleModel>();
            for (Vehicle vehicle : vehicles) {
                vehicleModels.add(transform(vehicle));
            }
        } else {
            vehicleModels = Collections.emptyList();
        }

        return vehicleModels;
    }
}
