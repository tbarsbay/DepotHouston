package com.tamerbarsbay.depothouston.presentation.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tamerbarsbay.depothouston.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_main_goto_route_list)
    void goToRouteList() {
        this.navigator.navigateToRouteList(this);
    }
}
