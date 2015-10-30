package com.tamerbarsbay.depothouston.presentation.view;

import com.tamerbarsbay.depothouston.presentation.model.IncidentModel;

import java.util.Collection;

public interface IncidentListView extends LoadDataView {

    void renderIncidentList(Collection<IncidentModel> incidentModels);
    void viewIncident(IncidentModel incidentModel); //TODO

}
