package com.tamerbarsbay.depothouston.test.presenter;

import android.content.Context;

import com.tamerbarsbay.depothouston.domain.interactor.GetStopsByRoute;
import com.tamerbarsbay.depothouston.domain.interactor.GetStopsNearLocationByRoute;
import com.tamerbarsbay.depothouston.presentation.mapper.StopModelDataMapper;
import com.tamerbarsbay.depothouston.presentation.presenter.StopListPresenter;
import com.tamerbarsbay.depothouston.presentation.view.StopListView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class StopListPresenterTest {

    private static final String FAKE_STOP_ID = "Stop01";
    private static final String FAKE_DIRECTION = "0";
    private static final double FAKE_LAT = 0;
    private static final double FAKE_LON = 0;
    private static final String FAKE_RADIUS_MI = ".25";

    private StopListPresenter stopListPresenter;

    private Context mockContext;
    private StopListView mockStopListView;
    private GetStopsByRoute mockGetStopsByRoute;
    private GetStopsNearLocationByRoute mockGetStopsNearLocationByRoute;

    @Before
    public void setup() throws Exception {
        mockContext = Mockito.mock(Context.class);
        mockStopListView = Mockito.mock(StopListView.class);
        mockGetStopsByRoute = Mockito.mock(GetStopsByRoute.class);
        mockGetStopsNearLocationByRoute = Mockito.mock(GetStopsNearLocationByRoute.class);
        stopListPresenter = new StopListPresenter(
                mockGetStopsByRoute,
                mockGetStopsNearLocationByRoute,
                Mockito.mock(StopModelDataMapper.class));
        stopListPresenter.setView(mockStopListView);
    }

    @Test
    public void testLoadStopsByRoute() {
        given(mockStopListView.getContext()).willReturn(mockContext);

        stopListPresenter.loadStopsByRoute(FAKE_STOP_ID, FAKE_DIRECTION);

        verify(mockStopListView).hideEmptyView();
        verify(mockStopListView).showLoadingView();
        verify(mockGetStopsByRoute).setParameters(FAKE_STOP_ID, FAKE_DIRECTION);
        verify(mockGetStopsByRoute).execute(any(Subscriber.class));
    }

    @Test
    public void testLoadStopsNearLocationByRoute() {
        given(mockStopListView.getContext()).willReturn(mockContext);

        stopListPresenter.loadNearbyStopsByRoute(FAKE_STOP_ID,
                FAKE_DIRECTION, FAKE_LAT, FAKE_LON, FAKE_RADIUS_MI);

        verify(mockStopListView).hideEmptyView();
        verify(mockStopListView).showLoadingView();
        verify(mockGetStopsNearLocationByRoute).setParameters(
                FAKE_STOP_ID,
                FAKE_DIRECTION,
                FAKE_LAT,
                FAKE_LON,
                FAKE_RADIUS_MI);
        verify(mockGetStopsNearLocationByRoute).execute(any(Subscriber.class));
    }
}
