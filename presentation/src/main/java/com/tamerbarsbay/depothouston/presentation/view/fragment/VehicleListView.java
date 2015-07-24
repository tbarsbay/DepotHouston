package com.tamerbarsbay.depothouston.presentation.view.fragment;

import com.tamerbarsbay.depothouston.presentation.model.VehicleModel;
import com.tamerbarsbay.depothouston.presentation.view.LoadDataView;

import java.util.Collection;

/**
 * Created by Tamer on 7/24/2015.
 */
public interface VehicleListView extends LoadDataView {

    void renderVehicleList(Collection<VehicleModel> vehicleModels);
    void viewVehicle(VehicleModel vehicleModel);

}
