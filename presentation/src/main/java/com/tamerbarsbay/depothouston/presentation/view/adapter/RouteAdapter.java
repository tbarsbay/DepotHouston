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

import butterknife.Bind;
import butterknife.ButterKnife;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    public interface OnItemClickListener {
        void onRouteItemClicked(RouteModel routeModel);
    }

    private List<RouteModel> routeModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public RouteAdapter(Context context, Collection<RouteModel> routeModels) {
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
    public RouteAdapter.RouteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.layoutInflater.inflate(R.layout.list_item_route, viewGroup, false);
        RouteViewHolder routeViewHolder = new RouteViewHolder(view);
        return routeViewHolder;
    }

    @Override
    public void onBindViewHolder(RouteAdapter.RouteViewHolder routeViewHolder, int position) {
        final RouteModel routeModel = this.routeModels.get(position);
        routeViewHolder.tvName.setText(routeModel.getRouteName());
        routeViewHolder.tvLongName.setText(routeModel.getLongName());
        routeViewHolder.tvType.setText(routeModel.getRouteType());
        routeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RouteAdapter.this.onItemClickListener != null) {
                    RouteAdapter.this.onItemClickListener.onRouteItemClicked(routeModel);
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
        @Bind(R.id.route_name)
        TextView tvName;

        @Bind(R.id.route_long_name)
        TextView tvLongName;

        @Bind(R.id.route_type)
        TextView tvType;

        public RouteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
