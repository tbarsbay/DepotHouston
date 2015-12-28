package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;
import java.util.List;

public class SimpleRouteArrayAdapter extends ArrayAdapter<RouteModel> {

    private List<RouteModel> routes;
    private LayoutInflater inflater;

    public SimpleRouteArrayAdapter(Context context, int layoutResId,
                                   int textViewResId, @NonNull Collection<RouteModel> routes) {
        super(context, layoutResId, textViewResId, (List<RouteModel>) routes);
        this.routes = (List<RouteModel>) routes;
        this.inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = inflater.inflate(R.layout.list_item_simple, null);
        }

        TextView tvName = (TextView) v.findViewById(R.id.tv_simple_list_item_name);
        if (tvName != null) {
            final RouteModel routeModel = routes.get(position);
            tvName.setText(routeModel.getRouteName());
        }

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
