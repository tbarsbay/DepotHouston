package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.VehicleEntity;
import com.tamerbarsbay.depothouston.domain.Vehicle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/23/2015.
 */
@Singleton
public class VehicleEntityDataMapper {

    @Inject
    public VehicleEntityDataMapper() {}

    public Vehicle transform(VehicleEntity vehicleEntity) {
        Vehicle vehicle = null;
        if (vehicleEntity != null) {
            vehicle = new Vehicle(vehicleEntity.getVehicleId());
            vehicle.setRouteName(vehicleEntity.getRouteName());
            vehicle.setBlock(vehicleEntity.getBlock());
            vehicle.setDelaySeconds(vehicleEntity.getDelaySeconds());
            vehicle.setDestinationName(vehicleEntity.getDestinationName());
            vehicle.setDirectionName(vehicleEntity.getDirectionName());
            vehicle.setIsMonitored(vehicleEntity.isMonitored());
            vehicle.setLat(vehicleEntity.getLat());
            vehicle.setLon(vehicleEntity.getLon());
            vehicle.setRouteId(vehicleEntity.getRouteId());
            vehicle.setVehicleReportTime(vehicleEntity.getVehicleReportTime());
        }
        return vehicle;
    }

    public List<Vehicle> transform(List<VehicleEntity> vehicleEntities) {
        List<Vehicle> vehicles = new ArrayList<Vehicle>();
        Vehicle vehicle = null;
        for (VehicleEntity vehicleEntity : vehicleEntities) {
            vehicle = transform(vehicleEntity);
            if (vehicle != null) {
                vehicles.add(vehicle);
            }
        }
        return vehicles;
    }
}
