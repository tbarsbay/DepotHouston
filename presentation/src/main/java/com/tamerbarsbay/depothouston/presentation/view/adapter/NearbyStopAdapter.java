package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NearbyStopAdapter extends RecyclerView.Adapter<NearbyStopAdapter.StopViewHolder> {

    public interface OnItemClickListener {
        void onStopClicked(StopModel stopModel);
    }

    private List<StopModel> stops;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public NearbyStopAdapter(Context context, Collection<StopModel> stops) {
        this.validateStops(stops);
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.stops = (List<StopModel>) stops;
    }

    @Override
    public int getItemCount() {
        return (this.stops != null) ? this.stops.size() : 0;
    }

    @Override
    public NearbyStopAdapter.StopViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = this.layoutInflater.inflate(R.layout.list_item_nearby_stop, viewGroup, false);
        StopViewHolder stopViewHolder = new StopViewHolder(view);
        return stopViewHolder;
    }

    @Override
    public void onBindViewHolder(NearbyStopAdapter.StopViewHolder stopViewHolder, int position) {
        final StopModel stop = stops.get(position);
        stopViewHolder.tvStopName.setText(stop.getName());
        stopViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NearbyStopAdapter.this.onItemClickListener != null) {
                    NearbyStopAdapter.this.onItemClickListener.onStopClicked(stop);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setStopsCollection(Collection<StopModel> stops) {
        validateStops(stops);
        this.stops = (List<StopModel>) stops;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateStops(Collection<StopModel> stops) {
        if (stops == null) {
            throw new IllegalArgumentException("Stop list cannot be null.");
        }
    }

    static class StopViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_nearby_stop_name)
        TextView tvStopName;

        @Bind(R.id.tv_nearby_stop_routes)
        TextView tvStopRoutes;

        @Bind(R.id.tv_nearby_stop_distance)
        TextView tvDistance;

        public StopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}