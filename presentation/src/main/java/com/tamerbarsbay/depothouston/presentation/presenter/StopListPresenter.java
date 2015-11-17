package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Stop;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.StopModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.StopListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class StopListPresenter implements Presenter {

    private StopListView stopListView;

    private final UseCase getStopsByRouteUseCase;
    private final StopModelDataMapper stopModelDataMapper;

    @Inject
    StopListPresenter(@Named("stopsByRoute") UseCase getStopsByRouteUseCase,
                       StopModelDataMapper stopModelDataMapper) {
        this.getStopsByRouteUseCase = getStopsByRouteUseCase;
        this.stopModelDataMapper = stopModelDataMapper;
    }

    public void setView(@NonNull StopListView stopListView) {
        this.stopListView = stopListView;
    }

    @Override public void resume() {}

    @Override public void pause() {}

    @Override public void destroy() {
        this.getStopsByRouteUseCase.unsubscribe();
    }

    public void initialize() {
        this.loadStopList();
    }

    /**
     * Loads all routes.
     */
    private void loadStopList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getRouteList();
    }

    public void onStopClicked(StopModel stopModel) {
        this.stopListView.viewStop(stopModel);
    }

    private void showViewLoading() {
        this.stopListView.showLoading();
    }

    private void hideViewLoading() {
        this.stopListView.hideLoading();
    }

    private void showViewRetry() {
        this.stopListView.showRetry();
    }

    private void hideViewRetry() {
        this.stopListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.stopListView.getContext(),
                errorBundle.getException());
        this.stopListView.showError(errorMessage);
    }

    private void showStopsInView(Collection<Stop> stops) {
        final Collection<StopModel> stopModels = this.stopModelDataMapper.transform(stops);
        this.stopListView.renderStopList(stopModels);
    }

    private void getRouteList() {
        this.getStopsByRouteUseCase.execute(new StopListSubscriber());
    }

    private final class StopListSubscriber extends DefaultSubscriber<List<Stop>> {

        @Override
        public void onNext(List<Stop> stops) {
            showStopsInView(stops);
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
