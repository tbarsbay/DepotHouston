package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.appwidget.AppWidgetManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SimpleAdapter;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RouteListFragment;
import com.tamerbarsbay.depothouston.presentation.view.fragment.StopListFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * An Activity where the user can select the route and stop for which to track arrivals
 * within a widget.
 */
public abstract class WidgetConfigurationActivity extends BaseActivity
    implements RouteListFragment.RouteListListener, StopListFragment.StopListListener {

    @Bind(R.id.rv_configure_widget_directions)
    RecyclerView rvDirections;

    @Bind(R.id.et_configure_widget_title)
    EditText etWidgetTitle;

    @Bind(R.id.sp_configure_widget_background_colors)
    Spinner spBackgroundColors;

    @Bind(R.id.tv_widget_title)
    TextView tvWidgetTitle;

    @Bind(R.id.layout_widget_arrivals)
    LinearLayout layoutWidgetArrivals;

    // Used to uniquely track each widget that the user places
    protected int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RouteModel selectedRoute;
    private String selectedDirection;
    private StopModel selectedStop;

    private ArrayList<String> backgroundColorOptions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If we get an Intent without the widget id, cancel the Activity
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            cancel();
            return;
        }

        //TODO temp, perhaps inject this?
        backgroundColorOptions.add("Dark Gray");
        backgroundColorOptions.add("White");
        backgroundColorOptions.add("Black");
        backgroundColorOptions.add("Navy");
        backgroundColorOptions.add("Blue");
        backgroundColorOptions.add("Red"); //TODO more colors
    }

    @Override
    public void onRouteClicked(RouteModel routeModel) {
        selectedRoute = routeModel;
        loadStepTwo(selectedRoute);
    }

    public void onDirectionClicked(String direction) {
        selectedDirection = direction;
        loadStepThree(selectedRoute, selectedDirection);
    }

    @Override
    public void onStopClicked(StopModel stopModel) {
        selectedStop = stopModel;
        loadStepFour(selectedRoute, selectedDirection, selectedStop);
    }

    /**
     * In step 1, the user selects a route.
     */
    protected void loadStepOne() {
        //TODO if the viewflipper is not on first page, move it
        addFragment(R.id.fl_configure_widget_routes, RouteListFragment.newInstance());
    }

    /**
     * In step 2, the user selects a direction.
     * @param route The route for which the user is selecting a direction
     */
    private void loadStepTwo(RouteModel route) {
        ArrayList<String> directions = new ArrayList<String>(); //TODO temp
        directions.add("0");
        directions.add("1");
        SimpleAdapter adapter = new SimpleAdapter(this, directions);
        adapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String direction) {
                loadStepThree(selectedRoute, direction);
            }
        });
        rvDirections.setAdapter(adapter);
    }

    /**
     * In step 3, the user selects a stop.
     * @param route The route for which the user is selecting a stop
     * @param direction The direction for which to load stops
     */
    private void loadStepThree(RouteModel route, String direction) {
        addFragment(R.id.fl_configure_widget_stops, StopListFragment.newInstance(route.getRouteId()));
    }

    /**
     * In step 4, the user customizes the look of the widget (title and background color).
     * @param route The route to be tracked in the widget
     * @param direction The direction to be tracked in the widget
     * @param stop The stop to be tracked in the widget
     */
    private void loadStepFour(RouteModel route, String direction, StopModel stop) {
        etWidgetTitle.setText(route.getRouteName() + " - " + stop.getName()); //TODO limit length of et
        populateBackgroundColorOptions();
    }

    private void populateBackgroundColorOptions() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_simple_white,
                R.id.tv_simple_list_item_name,
                backgroundColorOptions);
        adapter.setDropDownViewResource(R.layout.list_item_simple);
        if (spBackgroundColors != null) {
            spBackgroundColors.setAdapter(adapter);
        }
    }

    /**
     * Exit the widget creation flow.
     */
    @OnClick(R.id.btn_configure_widget_cancel)
    void cancel() {
        //TODO send to google analytics
        finish();
    }

    //TODO add option to choose from saved stops

    protected void showRefreshHint() {
        Toast.makeText(this, R.string.widget_refresh_hint, Toast.LENGTH_LONG).show();
    }

    private void startOver() {
        //TODO
    }

    protected abstract String getWidgetSizeAsString();


    /**
     * Build the widget and place it on the user's home screen.
     */
    @OnClick(R.id.btn_configure_widget_create)
    protected abstract void buildWidget();
}
