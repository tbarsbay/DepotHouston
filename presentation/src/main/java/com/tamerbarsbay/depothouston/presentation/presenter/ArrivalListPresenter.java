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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class ArrivalListPresenter implements Presenter {

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
        this.arrivalListView.showLoadingView();
    }

    private void hideViewLoading() {
        this.arrivalListView.hideLoadingView();
    }

    private void showViewRetry() {
        this.arrivalListView.showRetryView();
    }

    private void hideViewRetry() {
        this.arrivalListView.hideRetryView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.arrivalListView.getContext(),
                errorBundle.getException());
        this.arrivalListView.showError(errorMessage);
    }

    private void showArrivalsInView(Collection<Arrival> arrivals) {
        final Collection<ArrivalModel> arrivalModels = this.arrivalModelDataMapper.transform(arrivals);
        Collections.sort((List<ArrivalModel>)arrivalModels, new ArrivalComparator());
        this.arrivalListView.renderArrivalList(arrivalModels);
    }

    private void getArrivalList() {
        this.getArrivalsByStopUseCase.execute(new ArrivalListSubscriber());
    }

    private final class ArrivalListSubscriber extends DefaultSubscriber<List<Arrival>> {

        @Override
        public void onNext(List<Arrival> arrivals) {
            showArrivalsInView(arrivals);
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

    private class ArrivalComparator implements Comparator<ArrivalModel> {

        /**
         * Sorts arrivals by arrival time (soonest to latest).
         * @param arrival1
         * @param arrival2
         * @return
         */
        public int compare(ArrivalModel arrival1, ArrivalModel arrival2) {
            return Long.valueOf(arrival1.getMinsUntilArrival())
                    .compareTo(arrival2.getMinsUntilArrival());
        }
    }

}
