package com.tamerbarsbay.depothouston.presentation.presenter;

import android.support.annotation.NonNull;

import com.tamerbarsbay.depothouston.domain.Itinerary;
import com.tamerbarsbay.depothouston.domain.exception.ErrorBundle;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.domain.interactor.UseCase;
import com.tamerbarsbay.depothouston.presentation.exception.ErrorMessageFactory;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;
import com.tamerbarsbay.depothouston.presentation.mapper.ItineraryModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.model.ItineraryModel;
import com.tamerbarsbay.depothouston.presentation.view.ItineraryDetailsView;

import javax.inject.Inject;
import javax.inject.Named;

@PerActivity
public class ItineraryDetailsPresenter implements Presenter {

    private ItineraryDetailsView itineraryDetailsView;

    private final UseCase getItineraryDetailsUseCase;
    private final ItineraryModelDataMapper itineraryModelDataMapper;

    @Inject
    public ItineraryDetailsPresenter(@Named("itineraryDetails") UseCase getItineraryDetailsUseCase,
                                     ItineraryModelDataMapper itineraryModelDataMapper) {
        this.getItineraryDetailsUseCase = getItineraryDetailsUseCase;
        this.itineraryModelDataMapper = itineraryModelDataMapper;
    }

    public void setView(@NonNull ItineraryDetailsView itineraryDetailsView) {
        this.itineraryDetailsView = itineraryDetailsView;
    }

    @Override
    public void resume() {}

    @Override
    public void pause() {}

    @Override
    public void destroy() {
        this.getItineraryDetailsUseCase.unsubscribe();
    }

    public void initialize() {
        this.loadItineraryDetails();
    }

    private void loadItineraryDetails() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getItineraryDetails();
    }

    private void showViewLoading() {
        this.itineraryDetailsView.showLoadingView();
    }

    private void hideViewLoading() {
        this.itineraryDetailsView.hideLoadingView();
    }

    private void showViewRetry() {
        this.itineraryDetailsView.showRetryView();
    }

    private void hideViewRetry() {
        this.itineraryDetailsView.hideRetryView();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.itineraryDetailsView.getContext(),
                errorBundle.getException());
        this.itineraryDetailsView.showError(errorMessage);
    }

    private void showItineraryDetailsInView(Itinerary itinerary) {
        final ItineraryModel itineraryModel = this.itineraryModelDataMapper.transform(itinerary);
        this.itineraryDetailsView.renderItinerary(itineraryModel);
    }

    private void getItineraryDetails() {
        this.getItineraryDetailsUseCase.execute(new ItineraryDetailsSubscriber());
    }

    private final class ItineraryDetailsSubscriber extends DefaultSubscriber<Itinerary> {

        @Override
        public void onNext(Itinerary itinerary) {
            super.onNext(itinerary);
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }
    }
}
