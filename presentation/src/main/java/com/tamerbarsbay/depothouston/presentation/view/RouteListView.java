package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;

public interface RouteListView extends LoadDataView {

    void renderRouteList(Collection<RouteModel> routeModelCollection);
    void viewRoute(RouteModel routeModel);

}
