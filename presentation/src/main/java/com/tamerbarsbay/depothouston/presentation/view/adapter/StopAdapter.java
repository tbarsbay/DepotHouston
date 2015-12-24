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

public class StopAdapter extends RecyclerView.Adapter<StopAdapter.StopViewHolder> {

    public interface OnItemClickListener {
        void onStopItemClicked(StopModel stopModel);
    }

    private List<StopModel> stopModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public StopAdapter(Context context, Collection<StopModel> stopModels) {
        validateStopModels(stopModels);
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.stopModels = (List<StopModel>) stopModels;
    }

    @Override
    public int getItemCount() {
        return (stopModels != null) ? stopModels.size() : 0;
    }

    @Override
    public StopAdapter.StopViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.list_item_stop, viewGroup, false);
        StopViewHolder stopViewHolder = new StopViewHolder(view);
        return stopViewHolder;
    }

    @Override
    public void onBindViewHolder(StopAdapter.StopViewHolder stopViewHolder, int position) {
        final StopModel stopModel = stopModels.get(position);
        stopViewHolder.tvName.setText(stopModel.getName());
        stopViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StopAdapter.this.onItemClickListener != null) {
                    StopAdapter.this.onItemClickListener.onStopItemClicked(stopModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setStopsCollection(Collection<StopModel> stopModels) {
        validateStopModels(stopModels);
        this.stopModels = (List<StopModel>) stopModels;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateStopModels(Collection<StopModel> stopModels) {
        if (stopModels == null) {
            throw new IllegalArgumentException("Stop list cannot be null.");
        }
    }

    static class StopViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.stop_name)
        TextView tvName;

        public StopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
