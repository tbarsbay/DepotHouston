package com.tamerbarsbay.depothouston.data.entity.mapper;

import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.domain.Incident;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Tamer on 7/23/2015.
 */
@Singleton
public class IncidentEntityDataMapper {

    @Inject
    public IncidentEntityDataMapper() {}

    public Incident transform(IncidentEntity incidentEntity) {
        Incident incident = null;
        if (incidentEntity != null) {
            incident = new Incident(incidentEntity.getIncidentId());
            incident.setAffectedFrom(incidentEntity.getAffectedFrom());
            incident.setAffectedTo(incidentEntity.getAffectedTo());
            incident.setDescription(incidentEntity.getDescription());
            incident.setDuration(incidentEntity.getDuration());
            incident.setEmergencyText(incidentEntity.getEmergencyText());
            incident.setSeverity(incidentEntity.getSeverity());
            incident.setStatus(incidentEntity.getStatus());
        }
        return incident;
    }

    public List<Incident> transform(List<IncidentEntity> incidentEntities) {
        List<Incident> incidents = new ArrayList<Incident>();
        Incident incident = null;
        for (IncidentEntity incidentEntity : incidentEntities) {
            incident = transform(incidentEntity);
            if (incident != null) {
                incidents.add(incident);
            }
        }
        return incidents;
    }
}
