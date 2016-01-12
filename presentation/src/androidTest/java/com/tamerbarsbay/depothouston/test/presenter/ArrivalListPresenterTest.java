package com.tamerbarsbay.depothouston.test.presenter;

import android.content.Context;
import android.test.AndroidTestCase;

import com.tamerbarsbay.depothouston.domain.interactor.GetArrivalsByStop;
import com.tamerbarsbay.depothouston.domain.interactor.GetRoutesByStop;
import com.tamerbarsbay.depothouston.presentation.mapper.ArrivalModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.mapper.RouteModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.presenter.ArrivalListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.ActiveTrackingMenuView;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;

import org.mockito.Mock;
import org.mockito.Mockito;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class ArrivalListPresenterTest extends AndroidTestCase {

    private static final String FAKE_STOP_ID = "Stop01";

    private ArrivalListPresenter arrivalListPresenter;

    @Mock
    private Context mockContext;
    @Mock
    private ArrivalListView mockArrivalListView;
    @Mock
    private ActiveTrackingMenuView mockActiveTrackingMenuView;
    @Mock
    private GetArrivalsByStop mockGetArrivalsByStop;
    @Mock
    private GetRoutesByStop mockGetRoutesByStop;
    @Mock
    private ArrivalModelDataMapper mockArrivalModelDataMapper;
    @Mock
    private RouteModelDataMapper mockRouteModelDataMapper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mockContext = Mockito.mock(Context.class);
        mockArrivalListView = Mockito.mock(ArrivalListView.class);
        mockActiveTrackingMenuView = Mockito.mock(ActiveTrackingMenuView.class);
        mockGetArrivalsByStop = Mockito.mock(GetArrivalsByStop.class);
        mockGetRoutesByStop = Mockito.mock(GetRoutesByStop.class);
        mockRouteModelDataMapper = Mockito.mock(RouteModelDataMapper.class);
        arrivalListPresenter = new ArrivalListPresenter(
                mockGetArrivalsByStop,
                mockGetRoutesByStop,
                mockArrivalModelDataMapper,
                mockRouteModelDataMapper);
        arrivalListPresenter.setViews(mockArrivalListView, mockActiveTrackingMenuView);
    }

    public void testArrivalListPresenterLoadRoutesByStop() {
        given(mockArrivalListView.getContext()).willReturn(mockContext);

        arrivalListPresenter.loadRoutesByStop(FAKE_STOP_ID);

        verify(mockActiveTrackingMenuView).hideActiveTrackingRoutesErrorView();
        verify(mockActiveTrackingMenuView).showActiveTrackingRoutesLoadingView();
        verify(mockGetRoutesByStop).execute(any(Subscriber.class));
    }
}
