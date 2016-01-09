package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;

import java.util.ArrayList;

public class NavigationDrawerActivity extends BaseActivity {

    //TODO butterknife?
    private DrawerLayout mDrawerLayout;

    private Handler mHandler;
    private ViewGroup mDrawerItemsListContainer;

    // Symbols for navigation drawer. Not necessarily the items present,
    // just all the possible items.
    protected static final int NAVDRAWER_ITEM_ROUTE_LIST = 0;
    protected static final int NAVDRAWER_ITEM_MAP_SEARCH = 1;
    protected static final int NAVDRAWER_ITEM_SAVED_STOPS = 2;
    protected static final int NAVDRAWER_ITEM_RECENT_STOPS = 3;
    protected static final int NAVDRAWER_ITEM_SETTINGS = 4;
    protected static final int NAVDRAWER_ITEM_FEEDBACK = 5;
    protected static final int NAVDRAWER_ITEM_INVALID = -1;
    protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;

    // Titles for navigation drawer items (indices must correspond to those above)
    private static final int[] NAVDRAWER_TITLE_RES_ID = new int[] {
            R.string.nav_route_list,
            R.string.nav_map_search,
            R.string.nav_saved_stops,
            R.string.nav_recent_stops,
            R.string.nav_settings,
            R.string.nav_feedback
    };

    // Icons for navigation drawer items (indices must correspond to above array)
    private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
            R.drawable.ic_list,
            R.drawable.ic_map,
            R.drawable.ic_star,
            R.drawable.ic_history,
            R.drawable.ic_settings,
            R.drawable.ic_email
    };

    // Delay to launch nav drawer item, to allow close animation to play
    private static final int NAVDRAWER_LAUNCH_DELAY = 250;

    // List of navigation drawer items that were actually added to the navdrawer, in order
    private ArrayList<Integer> mNavDrawerItems = new ArrayList<Integer>();

    // Views that correspond to each navdrawer item, null if not yet created
    private View[] mNavDrawerItemViews = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        // Setup toolbar as action bar
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        int id = getSelfNavDrawerItem();
        if (id == NAVDRAWER_ITEM_INVALID || id == NAVDRAWER_ITEM_SEPARATOR) {
            return;
        }
        if (!isSpecialItem(id)) {
            // change the active item on the nav drawer list
            setSelectedNavDrawerItem(id);
        }
    }

    /**
     * Returns the navigation drawer item that corresponds to this Activity. Subclasses
     * of NavigationDrawerActivity override this to indicate what nav drawer item corresponds to them
     * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
     */
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_INVALID;
    }
    private void setupNavDrawer() {
        // What nav drawer item should be selected?
        int selfItem = getSelfNavDrawerItem();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout == null) {
            return;
        }

        ScrollView navDrawer = (ScrollView)mDrawerLayout.findViewById(R.id.navdrawer);
        if (selfItem == NAVDRAWER_ITEM_INVALID) {
            // Don't show a navigation drawer
            if (navDrawer != null) {
                ((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
            }
            mDrawerLayout = null;
            return;
        }

        if (mActionBarToolbar != null) {
            mActionBarToolbar.setNavigationIcon(R.drawable.ic_menu);
            mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }

        // skipped a lot here - listeners for the drawer opening and closing

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // populate the navigation drawer
        populateNavDrawer();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getActionBarToolbar();
    }

    protected boolean isNavDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    protected void closeNavDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private void populateNavDrawer() {
        mNavDrawerItems.clear();

        mNavDrawerItems.add(NAVDRAWER_ITEM_ROUTE_LIST);
        mNavDrawerItems.add(NAVDRAWER_ITEM_MAP_SEARCH);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SAVED_STOPS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_RECENT_STOPS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);
        mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);
        mNavDrawerItems.add(NAVDRAWER_ITEM_FEEDBACK);

        createNavDrawerItems();
    }

    @Override
    public void onBackPressed() {
        if (isNavDrawerOpen()) {
            closeNavDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void createNavDrawerItems() {
        mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
        if (mDrawerItemsListContainer == null) {
            return;
        }

        mNavDrawerItemViews = new View[mNavDrawerItems.size()];
        mDrawerItemsListContainer.removeAllViews();
        int i = 0;
        for (int itemId : mNavDrawerItems) {
            mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
            mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
            ++i;
        }
    }

    /**
     * Sets up the given navdrawer item's appearance to selected state.
     */
    private void setSelectedNavDrawerItem(int itemId) {
        if (mNavDrawerItemViews != null) {
            for (int i = 0; i < mNavDrawerItemViews.length; i++) {
                if (i < mNavDrawerItems.size()) {
                    int thisItemId = mNavDrawerItems.get(i);
                    formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
                }
            }
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupNavDrawer();
    }

    protected void goToNavDrawerItem(int item) {
        switch (item) {
            case NAVDRAWER_ITEM_ROUTE_LIST:
                navigator.navigateToRouteList(this, -1);
                break;
            case NAVDRAWER_ITEM_MAP_SEARCH:
                navigator.navigateToMapSearch(this, -1);
                break;
            case NAVDRAWER_ITEM_SAVED_STOPS:
                navigator.navigateToSavedStops(this, -1);
                break;
            case NAVDRAWER_ITEM_RECENT_STOPS:
                navigator.navigateToRecentStops(this, -1);
                break;
            case NAVDRAWER_ITEM_SETTINGS:
                navigator.navigateToSettings(this);
                break;
            case NAVDRAWER_ITEM_FEEDBACK:
                //sendFeedback(); TODO
                break;
        }
    }

    private void onNavDrawerItemClicked(final int itemId) {
        if (itemId == getSelfNavDrawerItem()) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNavDrawerItem(itemId);
            }
        }, NAVDRAWER_LAUNCH_DELAY);

        if (!isSpecialItem(itemId)) {
            // change the active item on the nav drawer list
            setSelectedNavDrawerItem(itemId);
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private boolean isSpecialItem(int itemId) {
        return itemId == NAVDRAWER_ITEM_SETTINGS
                || itemId == NAVDRAWER_ITEM_FEEDBACK;
    }

    private View makeNavDrawerItem(final int itemId, ViewGroup container) {
        boolean selected = getSelfNavDrawerItem() == itemId;
        int layoutToInflate = 0;
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            layoutToInflate = R.layout.navdrawer_item_separator;
        } else {
            layoutToInflate = R.layout.navdrawer_item;
        }
        View view = getLayoutInflater().inflate(layoutToInflate, container, false);

        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            return view;
        }

        ImageView iconView = (ImageView)view.findViewById(R.id.navdrawer_item_icon);
        TextView titleView = (TextView)view.findViewById(R.id.navdrawer_item_title);
        int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
                NAVDRAWER_ICON_RES_ID[itemId] : 0;
        int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
                NAVDRAWER_TITLE_RES_ID[itemId] : 0;

        iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
        if (itemId >= 0) {
            iconView.setImageResource(iconId);
        }
        titleView.setText(getString(titleId));

        formatNavDrawerItem(view, itemId, selected);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onNavDrawerItemClicked(itemId);
            }
        });

        return view;
    }

    private void formatNavDrawerItem(View view, int itemId, boolean selected) {
        if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
            return;
        }

        ImageView iconView = (ImageView) view.findViewById(R.id.navdrawer_item_icon);
        TextView titleView = (TextView) view.findViewById(R.id.navdrawer_item_title);

        // determine appearance based on whether item is selected
        Resources r = getResources();
        iconView.setColorFilter(selected ?
                r.getColor(R.color.nav_drawer_icon_tint_selected) :
                r.getColor(R.color.nav_drawer_icon_tint));
        titleView.setTextColor(selected ?
                r.getColor(R.color.nav_drawer_text_color_selected) :
                r.getColor(R.color.nav_drawer_text_color));
    }

}
