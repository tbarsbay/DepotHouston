package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.VehicleModel;

import java.util.Collection;

public interface VehicleListView extends LoadDataView {

    void renderVehicleList(Collection<VehicleModel> vehicleModels);
    void viewVehicle(VehicleModel vehicleModel);

}
