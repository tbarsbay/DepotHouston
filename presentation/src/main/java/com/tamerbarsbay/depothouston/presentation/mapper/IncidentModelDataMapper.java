package com.tamerbarsbay.depothouston.presentation.mapper;

import com.tamerbarsbay.depothouston.domain.Incident;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.model.IncidentModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.inject.Inject;

/**
 * Created by Tamer on 7/23/2015.
 */
@PerActivity
public class IncidentModelDataMapper {

    @Inject
    public IncidentModelDataMapper() {}

    public IncidentModel transform(Incident incident) {
        if (incident == null) {
            throw new IllegalArgumentException("Cannot transform a null incident.");
        }
        IncidentModel incidentModel = new IncidentModel(incident.getIncidentId());
        incidentModel.setAffectedFrom(incident.getAffectedFrom());
        incidentModel.setAffectedTo(incident.getAffectedTo());
        incidentModel.setDescription(incident.getDescription());
        incidentModel.setDuration(incident.getDuration());
        incidentModel.setEmergencyText(incident.getEmergencyText());
        incidentModel.setSeverity(incident.getSeverity());
        incidentModel.setStatus(incident.getStatus());
        return incidentModel;
    }

    public Collection<IncidentModel> transform(Collection<Incident> incidents) {
        Collection<IncidentModel> incidentModels;

        if (incidents != null && !incidents.isEmpty()) {
            incidentModels = new ArrayList<IncidentModel>();
            for (Incident incident : incidents) {
                incidentModels.add(transform(incident));
            }
        } else {
            incidentModels = Collections.emptyList();
        }

        return incidentModels;
    }
}
