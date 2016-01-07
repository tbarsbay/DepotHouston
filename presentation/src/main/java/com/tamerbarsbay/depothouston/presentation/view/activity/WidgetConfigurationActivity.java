package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerRouteComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.DaggerStopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.RouteComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.StopComponent;
import com.tamerbarsbay.depothouston.presentation.internal.di.modules.StopModule;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.model.WidgetModel;
import com.tamerbarsbay.depothouston.presentation.receiver.WidgetProvider;
import com.tamerbarsbay.depothouston.presentation.util.PrefUtils;
import com.tamerbarsbay.depothouston.presentation.util.WidgetUtils;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SimpleAdapter;
import com.tamerbarsbay.depothouston.presentation.view.fragment.RouteListFragment;
import com.tamerbarsbay.depothouston.presentation.view.fragment.StopListFragment;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * An Activity where the user can select the route and stop for which to track arrivals
 * within a widget.
 */
public abstract class WidgetConfigurationActivity extends BaseActivity
    implements RouteListFragment.RouteListListener, StopListFragment.StopListListener {

    @Bind(R.id.vf_configure_widget)
    ViewFlipper vf;

    @Bind(R.id.tv_configure_widget_step_number)
    TextView tvStepNumber;

    @Bind(R.id.tv_configure_widget_step_description)
    TextView tvStepDescription;

    @Bind(R.id.iv_configure_widget_step_one_icon)
    ImageView ivStepOneIcon;

    @Bind(R.id.iv_configure_widget_step_two_icon)
    ImageView ivStepTwoIcon;

    @Bind(R.id.iv_configure_widget_step_three_icon)
    ImageView ivStepThreeIcon;

    @Bind(R.id.iv_configure_widget_step_four_icon)
    ImageView ivStepFourIcon;

    @BindDrawable(R.drawable.ic_circle_filled)
    Drawable circleFilledIcon;

    @BindDrawable(R.drawable.ic_circle_outline)
    Drawable circleOutlineIcon;

    @Bind(R.id.rv_configure_widget_directions)
    RecyclerView rvDirections;

    @Bind(R.id.et_configure_widget_title)
    EditText etWidgetTitle;

    @Bind(R.id.sp_configure_widget_background_colors)
    Spinner spBackgroundColors;

    @Bind(R.id.vs_configure_widget_preview)
    ViewStub vsWidgetPreview;

    View widgetPreview;
    TextView tvWidgetPreviewTitle;
    LinearLayout layoutWidgetPreviewArrivals;

    private final ImageView[] STEP_ICONS = new ImageView[] {
            ivStepOneIcon,
            ivStepTwoIcon,
            ivStepThreeIcon,
            ivStepFourIcon
    };

    // Used to uniquely track each widget that the user places
    protected int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private RouteModel selectedRoute;
    private String selectedDirection;
    private StopModel selectedStop;

    private RouteComponent routeComponent;
    private StopComponent stopComponent;

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

        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        // Show the appropriate widget preview layout given the user's size selection
        vsWidgetPreview.setLayoutResource(getWidgetSize() == WidgetUtils.SIZE_1X1
                ? R.layout.widget_layout_1x1
                : R.layout.widget_layout_2x1);
        widgetPreview = vsWidgetPreview.inflate();
        tvWidgetPreviewTitle = (TextView) widgetPreview.findViewById(R.id.tv_widget_title);
        layoutWidgetPreviewArrivals = (LinearLayout) widgetPreview.findViewById(R.id.layout_widget_arrivals);

        //TODO set focus to scrollview??

        vf.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_fade_in));
        vf.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left_fade_out));

        loadStepOne();
    }

    private void initializeRouteInjector() {
        routeComponent = DaggerRouteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    private void initializeStopInjector() {
        stopComponent = DaggerStopComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .stopModule(new StopModule(selectedRoute.getRouteId()))
                .build();
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
        // Set step icons
        ivStepOneIcon.setImageDrawable(circleFilledIcon);
        ivStepTwoIcon.setImageDrawable(circleOutlineIcon);
        ivStepThreeIcon.setImageDrawable(circleOutlineIcon);
        ivStepFourIcon.setImageDrawable(circleOutlineIcon);

        // Set step number and description
        tvStepNumber.setText(R.string.step_one);
        tvStepDescription.setText(R.string.configure_widget_step_1_description);

        // Load UI of the first page
        vf.setDisplayedChild(0);
        initializeRouteInjector();
        addFragment(R.id.fl_configure_widget_routes, RouteListFragment.newInstance());
    }

    /**
     * In step 2, the user selects a direction.
     * @param route The route for which the user is selecting a direction
     */
    private void loadStepTwo(RouteModel route) {
        // Set step icon
        ivStepTwoIcon.setImageDrawable(circleFilledIcon);

        // Set step number and description
        tvStepNumber.setText(R.string.step_two);
        tvStepDescription.setText(R.string.configure_widget_step_2_description);

        // Populate direction options
        ArrayList<String> directions = new ArrayList<String>(); //TODO temp
        directions.add("0");
        directions.add("1");
        SimpleAdapter adapter = new SimpleAdapter(this, directions);
        adapter.setOnItemClickListener(new SimpleAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(String direction) {
                onDirectionClicked(direction);
            }
        });
        rvDirections.setLayoutManager(new LinearLayoutManager(this));
        rvDirections.setAdapter(adapter);

        // Load UI of the second page
        vf.setDisplayedChild(1);
    }

    /**
     * In step 3, the user selects a stop.
     * @param route The route for which the user is selecting a stop
     * @param direction The direction for which to load stops
     */
    private void loadStepThree(RouteModel route, String direction) {
        // Set step icon
        ivStepThreeIcon.setImageDrawable(circleFilledIcon);

        // Set step number and description
        tvStepNumber.setText(R.string.step_three);
        tvStepDescription.setText(R.string.configure_widget_step_3_description);

        // Load UI of the third page
        vf.setDisplayedChild(2);
        initializeStopInjector();
        addFragment(R.id.fl_configure_widget_stops, StopListFragment.newInstance(route.getRouteId()));
    }

    /**
     * In step 4, the user customizes the look of the widget (title and background color).
     * @param route The route to be tracked in the widget
     * @param direction The direction to be tracked in the widget
     * @param stop The stop to be tracked in the widget
     */
    private void loadStepFour(RouteModel route, String direction, StopModel stop) {
        // Set step icon
        ivStepFourIcon.setImageDrawable(circleFilledIcon);

        // Set step number and description
        tvStepNumber.setText(R.string.step_four);
        tvStepDescription.setText(R.string.configure_widget_step_4_description);

        // Load UI of the fourth page
        String defaultText = route.getRouteName() + " - " + stop.getName();
        etWidgetTitle.setText(defaultText); //TODO limit length of et
        updateWidgetPreviewTitle(defaultText);

        populateBackgroundColorOptions();
        spBackgroundColors.setSelection(0);
        vf.setDisplayedChild(3);
    }

    @OnTextChanged(R.id.et_configure_widget_title)
    void updateWidgetPreviewTitle(CharSequence title) {
        tvWidgetPreviewTitle.setText(title);
    }

    @OnItemSelected(R.id.sp_configure_widget_background_colors)
    void updateWidgetPreviewBackgroundColors(int selectedBgIndex) {
        tvWidgetPreviewTitle.setBackgroundColor(WidgetUtils.getPrimaryColorInt(this, selectedBgIndex));
        layoutWidgetPreviewArrivals.setBackgroundColor(WidgetUtils.getSecondaryColorInt(this, selectedBgIndex));
    }

    private void populateBackgroundColorOptions() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                R.layout.list_item_simple_white,
                R.id.tv_simple_list_item_name,
                new ArrayList<String>(Arrays.asList(WidgetUtils.BG_COLOR_STRINGS)));
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

    //TODO onclick startoverbtn
    private void startOver() {
        loadStepOne();
    }

    protected abstract int getWidgetSize();

    /**
     * Build the widget and place it on the user's home screen.
     */
    @OnClick(R.id.btn_configure_widget_create)
    void buildWidget() {
        //AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

        WidgetModel widgetModel = new WidgetModel(
                widgetId,
                selectedRoute.getRouteId(),
                selectedDirection,
                selectedStop.getStopId(),
                etWidgetTitle.getText().toString(),
                getWidgetSize(),
                spBackgroundColors.getSelectedItemPosition());
        PrefUtils.saveWidget(this, widgetModel);

        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, WidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {widgetId});
        sendBroadcast(intent);
        //WidgetProvider.executeArrivalsRequest(this, widgetManager, widgetId);

        showRefreshHint();

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    public RouteComponent getRouteComponent() {
        return routeComponent;
    }

    @Override
    public StopComponent getStopComponent() {
        return stopComponent;
    }
}
