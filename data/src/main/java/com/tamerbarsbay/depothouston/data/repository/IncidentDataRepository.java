package com.tamerbarsbay.depothouston.data.repository;

import android.content.Context;

import com.tamerbarsbay.depothouston.data.entity.IncidentEntity;
import com.tamerbarsbay.depothouston.data.entity.mapper.IncidentEntityDataMapper;
import com.tamerbarsbay.depothouston.data.net.RestApi;
import com.tamerbarsbay.depothouston.domain.Incident;
import com.tamerbarsbay.depothouston.domain.repository.IncidentRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

@Singleton
public class IncidentDataRepository implements IncidentRepository {

    private Context context;
    private IncidentEntityDataMapper incidentEntityDataMapper;

    private final Func1<List<IncidentEntity>, List<Incident>> incidentEntityListMapper =
            new Func1<List<IncidentEntity>, List<Incident>>() {
                @Override
                public List<Incident> call(List<IncidentEntity> incidentEntities) {
                    return IncidentDataRepository.this.
                            incidentEntityDataMapper.transform(incidentEntities);
                }
            };

    @Inject
    public IncidentDataRepository(Context context,
                                  IncidentEntityDataMapper incidentEntityDataMapper) {
        this.context = context;
        this.incidentEntityDataMapper = incidentEntityDataMapper;
    }

    @Override
    public Observable<List<Incident>> getIncidents() {
        // Incident lists will always come from the Metro API and not the local cache.
        // Therefore we can skip the creation of an IncidentDataStore and just use the RestApi.
        RestApi restApi = new RestApi(this.context);
        return restApi.incidents().map(incidentEntityListMapper);
    }
}
