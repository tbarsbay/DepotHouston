package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(String item);
    }

    private List<String> objects;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    public SimpleAdapter(Context context, @NonNull Collection<String> objects) {
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects = (List<String>) objects;
    }

    @Override
    public int getItemCount() {
        return (this.objects != null) ? this.objects.size() : 0;
    }

    @Override
    public SimpleAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = this.layoutInflater.inflate(R.layout.list_item_simple, viewGroup, false);
        ViewHolder routeViewHolder = new ViewHolder(view);
        return routeViewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleAdapter.ViewHolder viewHolder, int position) {
        final String item = this.objects.get(position);
        viewHolder.tvName.setText(item);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SimpleAdapter.this.onItemClickListener != null) {
                    SimpleAdapter.this.onItemClickListener.onItemClicked(item);
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