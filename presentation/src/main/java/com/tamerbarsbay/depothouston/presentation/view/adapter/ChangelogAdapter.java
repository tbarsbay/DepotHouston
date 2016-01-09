package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.ChangelogItemModel;

import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChangelogAdapter extends RecyclerView.Adapter<ChangelogAdapter.ViewHolder> {

    private List<ChangelogItemModel> changes;
    private final LayoutInflater layoutInflater;

    public ChangelogAdapter(Context context, @NonNull Collection<ChangelogItemModel> changes) {
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.changes = (List<ChangelogItemModel>) changes;
    }

    @Override
    public int getItemCount() {
        return (changes != null) ? changes.size() : 0;
    }

    @Override
    public ChangelogAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = layoutInflater.inflate(R.layout.list_item_changelog, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChangelogAdapter.ViewHolder viewHolder, int position) {
        final ChangelogItemModel change = changes.get(position);
        viewHolder.tvVersion.setText("v" + change.getVersion());
        viewHolder.tvDate.setText("(" + change.getDate() + ")");
        viewHolder.tvChanges.setText(change.getChanges());
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_changelog_item_version)
        TextView tvVersion;

        @Bind(R.id.tv_changelog_item_date)
        TextView tvDate;

        @Bind(R.id.tv_changelog_item_changes)
        TextView tvChanges;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
