package com.tamerbarsbay.depothouston.presentation.presenter;

import com.tamerbarsbay.depothouston.domain.Itinerary;
import com.tamerbarsbay.depothouston.domain.interactor.DefaultSubscriber;
import com.tamerbarsbay.depothouston.presentation.internal.di.PerActivity;

/**
 * Created by Tamer on 7/24/2015.
 */
@PerActivity
public class BuildItineraryPresenter extends DefaultSubscriber<Itinerary> implements Presenter {

    //TODO

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

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
