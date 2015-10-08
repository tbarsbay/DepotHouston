package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;

import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArrivalListAdapter extends RecyclerView.Adapter<ArrivalListAdapter.ArrivalViewHolder> {

    public interface OnItemClickListener {
        void onArrivalItemClicked(ArrivalModel arrivalModel);
    }

    private List<ArrivalModel> arrivalModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    private static final String ARRIVAL_IN_MINS_TEMPLATE = "%sm";

    public ArrivalListAdapter(Context context, Collection<ArrivalModel> arrivalModels) {
        this.validateArrivalModels(arrivalModels);
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrivalModels = (List<ArrivalModel>) arrivalModels;
    }

    @Override
    public int getItemCount() {
        return (this.arrivalModels != null) ? this.arrivalModels.size() : 0;
    }

    @Override
    public ArrivalListAdapter.ArrivalViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = this.layoutInflater.inflate(R.layout.list_item_arrival, viewGroup, false);
        ArrivalViewHolder arrivalViewHolder = new ArrivalViewHolder(view);
        return arrivalViewHolder;
    }

    @Override
    public void onBindViewHolder(ArrivalListAdapter.ArrivalViewHolder arrivalViewHolder, int position) {
        final ArrivalModel arrivalModel = this.arrivalModels.get(position);
        String arrivalTime = arrivalModel.getMinsUntilArrival();
        //TODO handle 0, 1m with "Arriving now"
        arrivalViewHolder.tvArrivalTime.setText(String.format(ARRIVAL_IN_MINS_TEMPLATE, arrivalTime));
        arrivalViewHolder.tvRouteName.setText(arrivalModel.getRouteName());
        arrivalViewHolder.tvDestinationName.setText(arrivalModel.getDestinationName());
        arrivalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArrivalListAdapter.this.onItemClickListener != null) {
                    ArrivalListAdapter.this.onItemClickListener.onArrivalItemClicked(arrivalModel);
                }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setArrivalsCollection(Collection<ArrivalModel> arrivalModels) {
        this.validateArrivalModels(arrivalModels);
        this.arrivalModels = (List<ArrivalModel>) arrivalModels;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateArrivalModels(Collection<ArrivalModel> arrivalModels) {
        if (arrivalModels == null) {
            throw new IllegalArgumentException("Arrival list cannot be null.");
        }
    }

static class ArrivalViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.arrival_time)
    TextView tvArrivalTime;

    @InjectView(R.id.arrival_route_name)
    TextView tvRouteName;

    @InjectView(R.id.arrival_destination_name)
    TextView tvDestinationName;
    
    //TODO

    public ArrivalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
    }
}
}
