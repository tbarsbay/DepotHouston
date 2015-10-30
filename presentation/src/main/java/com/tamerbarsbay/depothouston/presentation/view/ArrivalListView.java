package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;

import java.util.Collection;

public interface ArrivalListView extends LoadDataView {

    void renderArrivalList(Collection<ArrivalModel> arrivalModels);
    //TODO followArrival?
}
