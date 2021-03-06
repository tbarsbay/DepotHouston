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

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArrivalAdapter extends RecyclerView.Adapter<ArrivalAdapter.ArrivalViewHolder> {

    public interface OnItemClickListener {
        void onArrivalItemClicked(ArrivalModel arrivalModel);
    }

    private final Context context;
    private List<ArrivalModel> arrivalModels;
    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;

    private static final String ARRIVAL_IN_MINS_TEMPLATE = "%sm";

    public ArrivalAdapter(Context context, Collection<ArrivalModel> arrivalModels) {
        this.validateArrivalModels(arrivalModels);
        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.arrivalModels = (List<ArrivalModel>) arrivalModels;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return (this.arrivalModels != null) ? this.arrivalModels.size() : 0;
    }

    @Override
    public ArrivalAdapter.ArrivalViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = this.layoutInflater.inflate(R.layout.list_item_arrival, viewGroup, false);
        ArrivalViewHolder arrivalViewHolder = new ArrivalViewHolder(view);
        return arrivalViewHolder;
    }

    @Override
    public void onBindViewHolder(ArrivalAdapter.ArrivalViewHolder arrivalViewHolder, int position) {
        final ArrivalModel arrivalModel = this.arrivalModels.get(position);
        arrivalViewHolder.tvMinutesUntilArrival.setText(getArrivalTextToShow(arrivalModel));
        arrivalViewHolder.tvMinutesUntilArrival.setTextColor(getArrivalTextColor(arrivalModel));
        arrivalViewHolder.tvArrivalTime.setText(arrivalModel.getFormattedTime(arrivalModel.getUtcArrivalTime()));
        arrivalViewHolder.tvRouteName.setText(arrivalModel.getRouteName());
        arrivalViewHolder.tvDestinationName.setText(arrivalModel.getDestinationName());
        arrivalViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ArrivalAdapter.this.onItemClickListener != null) {
                    ArrivalAdapter.this.onItemClickListener.onArrivalItemClicked(arrivalModel);
                }
            }
        });
    }

    private int getArrivalTextColor(ArrivalModel arrivalModel) {
        return arrivalModel.isRealTime() ?
                context.getResources().getColor(R.color.prediction_real_time) :
                context.getResources().getColor(R.color.prediction_scheduled);
    }

    private String getArrivalTextToShow(ArrivalModel arrivalModel) {
        long minsUntilArrival = arrivalModel.getMinsUntilArrival();
        if (minsUntilArrival == 0 || minsUntilArrival == 1) {
            return "Due";
        } else {
            return String.format(ARRIVAL_IN_MINS_TEMPLATE, String.valueOf(minsUntilArrival));
        }
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
    @Bind(R.id.arrival_minutes_until_arrival)
    TextView tvMinutesUntilArrival;

    @Bind(R.id.arrival_route_name)
    TextView tvRouteName;

    @Bind(R.id.arrival_destination_name)
    TextView tvDestinationName;
    
    @Bind(R.id.arrival_time)
    TextView tvArrivalTime;

    public ArrivalViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
}
