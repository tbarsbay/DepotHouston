package com.tamerbarsbay.depothouston.test.presenter;

import android.content.Context;

import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStop;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesByStop;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.mapper.RouteModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.presenter.ArrivalListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.ActiveTrackingMenuView;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class ArrivalListPresenterTest {

    private static final String FAKE_STOP_ID = "Stop01";

    private ArrivalListPresenter arrivalListPresenter;

    private Context mockContext;
    private ArrivalListView mockArrivalListView;
    private ActiveTrackingMenuView mockActiveTrackingMenuView;
    private GetArrivalsByStop mockGetArrivalsByStop;
    private GetRoutesByStop mockGetRoutesByStop;

    @Before
    public void setup() throws Exception {
        mockContext = Mockito.mock(Context.class);
        mockArrivalListView = Mockito.mock(ArrivalListView.class);
        mockActiveTrackingMenuView = Mockito.mock(ActiveTrackingMenuView.class);
        mockGetArrivalsByStop = Mockito.mock(GetArrivalsByStop.class);
        mockGetRoutesByStop = Mockito.mock(GetRoutesByStop.class);
        arrivalListPresenter = new ArrivalListPresenter(
                mockGetArrivalsByStop,
                mockGetRoutesByStop,
                Mockito.mock(ArrivalModelDataMapper.class),
                Mockito.mock(RouteModelDataMapper.class));
        arrivalListPresenter.setViews(mockArrivalListView, mockActiveTrackingMenuView);
    }

    @Test
    public void testLoadRoutesByStop() {
        given(mockArrivalListView.getContext()).willReturn(mockContext);

        arrivalListPresenter.loadRoutesByStop(FAKE_STOP_ID);

        verify(mockActiveTrackingMenuView).hideActiveTrackingRoutesErrorView();
        verify(mockActiveTrackingMenuView).showActiveTrackingRoutesLoadingView();
        verify(mockGetRoutesByStop).setParameters(FAKE_STOP_ID);
        verify(mockGetRoutesByStop).execute(any(Subscriber.class));
    }

    @Test
    public void testLoadArrivalsByStop() {
        given(mockArrivalListView.getContext()).willReturn(mockContext);

        arrivalListPresenter.loadArrivalsByStop(FAKE_STOP_ID);

        verify(mockArrivalListView).hideRetryView();
        //TODO verify hide empty
        verify(mockArrivalListView).showLoadingView();
        verify(mockGetArrivalsByStop).setParameters(FAKE_STOP_ID);
        verify(mockGetArrivalsByStop).execute(any(Subscriber.class));
    }

}
