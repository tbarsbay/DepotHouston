package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.RecentStopModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecentStopAdapter extends RecyclerView.Adapter<RecentStopAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onStopClicked(RecentStopModel recentStop);
    }

    private List<RecentStopModel> recentStops;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public RecentStopAdapter(Context context, @NonNull Collection<RecentStopModel> recentStops) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.recentStops = (List<RecentStopModel>) recentStops;
    }

    @Override
    public int getItemCount() {
        return (recentStops != null) ? recentStops.size() : 0;
    }

    @Override
    public RecentStopAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.list_item_recent_stop, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecentStopAdapter.ViewHolder viewHolder, int position) {
        final RecentStopModel recentStop = recentStops.get(position);
        viewHolder.tvName.setText(recentStop.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecentStopAdapter.this.onItemClickListener != null) {
                    RecentStopAdapter.this.onItemClickListener.onStopClicked(recentStop);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_recent_stop_name)
        TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
