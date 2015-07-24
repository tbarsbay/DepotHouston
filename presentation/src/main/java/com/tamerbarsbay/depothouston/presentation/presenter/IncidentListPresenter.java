package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.Nullable;

import com.tamerbarsbay.depothouston.domain.Incident;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.IncidentModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.IncidentModel;
import com.tamerbarsbay.depothouston.presentation.view.IncidentListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Tamer on 7/24/2015.
 */
@PerActivity
public class IncidentListPresenter extends DefaultSubscriber<List<Incident>> implements Presenter {

    private IncidentListView incidentListView;

    private final UseCase getIncidentListUseCase;
    private final IncidentModelDataMapper incidentModelDataMapper;

    @Inject
    public IncidentListPresenter(@Named("incidentList") UseCase getIncidentListUseCase,
                                 IncidentModelDataMapper incidentModelDataMapper) {
        this.getIncidentListUseCase = getIncidentListUseCase;
        this.incidentModelDataMapper = incidentModelDataMapper;
    }

    public void setView(@Nullable IncidentListView incidentListView) {
        this.incidentListView = incidentListView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.getIncidentListUseCase.unsubscribe();
    }

    public void initialize() {
        this.loadIncidentList();
    }

    private void loadIncidentList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getIncidentList();
    }

    public void onIncidentClicked(IncidentModel incidentModel) {
        //TODO
    }

    private void showViewLoading() {
        this.incidentListView.showLoading();
    }

    private void hideViewLoading() {
        this.incidentListView.hideLoading();
    }

    private void showViewRetry() {
        this.incidentListView.showRetry();
    }

    private void hideViewRetry() {
        this.incidentListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.incidentListView.getContext(),
                errorBundle.getException());
        this.incidentListView.showError(errorMessage);
    }

    private void showIncidentsInView(Collection<Incident> incidents) {
        final Collection<IncidentModel> incidentModels = this.incidentModelDataMapper.transform(incidents);
        this.incidentListView.renderIncidentList(incidentModels);
    }

    private void getIncidentList() {
        this.getIncidentListUseCase.execute(this);
    }

    @Override
    public void onNext(List<Incident> incidents) {
        this.showIncidentsInView(incidents);
    }

    @Override
    public void onCompleted() {
        this.hideViewLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.hideViewLoading();
        this.showErrorMessage(new DefaultErrorBundle((Exception)e));
        this.showViewRetry();
    }
}
