package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Arrival;
import com.tamerbarsbay.depothouston.domain.exception.DefaultErrorBundle;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Tamer on 7/24/2015.
 */
@PerActivity
public class ArrivalListPresenter extends DefaultSubscriber<List<Arrival>> implements Presenter {

    private ArrivalListView arrivalListView;

    private final UseCase getArrivalsByStopUseCase;
    private final ArrivalModelDataMapper arrivalModelDataMapper;

    @Inject
    ArrivalListPresenter(@Named("arrivalsByStop") UseCase getArrivalsByStopUseCase,
                         ArrivalModelDataMapper arrivalModelDataMapper) {
        this.getArrivalsByStopUseCase = getArrivalsByStopUseCase;
        this.arrivalModelDataMapper = arrivalModelDataMapper;
    }

    public void setView(@NonNull ArrivalListView arrivalListView) {
        this.arrivalListView = arrivalListView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.getArrivalsByStopUseCase.unsubscribe();
    }

    public void initialize() {
        this.loadArrivalList();
    }

    private void loadArrivalList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getArrivalList();
    }

    public void onArrivalClicked(ArrivalModel arrivalModel) {
        //TODO necessary? followArrival?
    }

    private void showViewLoading() {
        this.arrivalListView.showLoading();
    }

    private void hideViewLoading() {
        this.arrivalListView.hideLoading();
    }

    private void showViewRetry() {
        this.arrivalListView.showRetry();
    }

    private void hideViewRetry() {
        this.arrivalListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.arrivalListView.getContext(),
                errorBundle.getException());
        this.arrivalListView.showError(errorMessage);
    }

    private void showArrivalsInView(Collection<Arrival> arrivals) {
        final Collection<ArrivalModel> arrivalModels = this.arrivalModelDataMapper.transform(arrivals);
        this.arrivalListView.renderArrivalList(arrivalModels);
    }

    private void getArrivalList() {
        this.getArrivalsByStopUseCase.execute(this);
    }

    @Override
    public void onNext(List<Arrival> arrivals) {
        this.showArrivalsInView(arrivals);
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
