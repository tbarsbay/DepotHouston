package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RouteModel;

import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Tamer on 7/23/2015.
 */
public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteViewHolder> {

    public interface OnItemClickListener {
        void onRouteItemClicked(RouteModel routeModel);
    }

    private List<RouteModel> routeModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public RouteListAdapter(Context context, Collection<RouteModel> routeModels) {
        this.validateRouteModels(routeModels);
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.routeModels = (List<RouteModel>) routeModels;
    }

    @Override
    public int getItemCount() {
        return (this.routeModels != null) ? this.routeModels.size() : 0;
    }

    @Override
    public RouteListAdapter.RouteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.layoutInflater.inflate(R.layout.list_item_route, viewGroup, false);
        RouteViewHolder routeViewHolder = new RouteViewHolder(view);
        return routeViewHolder;
    }

    @Override
    public void onBindViewHolder(RouteListAdapter.RouteViewHolder routeViewHolder, int position) {
        final RouteModel routeModel = this.routeModels.get(position);
        routeViewHolder.tvName.setText(routeModel.getRouteName());
        routeViewHolder.tvLongName.setText(routeModel.getLongName());
        routeViewHolder.tvType.setText(routeModel.getRouteType());
        routeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RouteListAdapter.this.onItemClickListener != null) {
                    RouteListAdapter.this.onItemClickListener.onRouteItemClicked(routeModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setRoutesCollection(Collection<RouteModel> routeModels) {
        this.validateRouteModels(routeModels);
        this.routeModels = (List<RouteModel>) routeModels;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateRouteModels(Collection<RouteModel> routeModels) {
        if (routeModels == null) {
            throw new IllegalArgumentException("Route list cannot be null.");
        }
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.route_name)
        TextView tvName;

        @InjectView(R.id.route_long_name)
        TextView tvLongName;

        @InjectView(R.id.route_type)
        TextView tvType;

        public RouteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
