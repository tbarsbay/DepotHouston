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

@PerActivity
public class IncidentListPresenter implements Presenter {

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
        this.incidentListView.showLoadingView();
    }

    private void hideViewLoading() {
        this.incidentListView.hideLoadingView();
    }

    private void showViewRetry() {
        this.incidentListView.showRetryView();
    }

    private void hideViewRetry() {
        this.incidentListView.hideRetryView();
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
        this.getIncidentListUseCase.execute(new IncidentListSubscriber());
    }

    private final class IncidentListSubscriber extends DefaultSubscriber<List<Incident>> {

        @Override
        public void onNext(List<Incident> incidents) {
            showIncidentsInView(incidents);
        }

        @Override
        public void onCompleted() {
            hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            hideViewLoading();
            showErrorMessage(new DefaultErrorBundle((Exception) e));
            showViewRetry();
        }
    }
}
