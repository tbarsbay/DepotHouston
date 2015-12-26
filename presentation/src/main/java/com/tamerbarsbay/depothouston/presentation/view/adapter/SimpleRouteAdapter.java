package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class SimpleRouteAdapter extends RecyclerView.Adapter<SimpleRouteAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onRouteItemClicked(RouteModel routeModel);
    }

    private List<RouteModel> routeModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public SimpleRouteAdapter(Context context, @NonNull Collection<RouteModel> routeModels) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.routeModels = (List<RouteModel>) routeModels;
    }

    @Override
    public int getItemCount() {
        return (this.routeModels != null) ? this.routeModels.size() : 0;
    }

    @Override
    public SimpleRouteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.layoutInflater.inflate(R.layout.list_item_simple, viewGroup, false);
        ViewHolder routeViewHolder = new ViewHolder(view);
        return routeViewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleRouteAdapter.ViewHolder routeViewHolder, int position) {
        final RouteModel routeModel = this.routeModels.get(position);
        routeViewHolder.tvName.setText(routeModel.getRouteName());
        routeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SimpleRouteAdapter.this.onItemClickListener != null) {
                    SimpleRouteAdapter.this.onItemClickListener.onRouteItemClicked(routeModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnItemClickListener (@NonNull OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_simple_list_item_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
