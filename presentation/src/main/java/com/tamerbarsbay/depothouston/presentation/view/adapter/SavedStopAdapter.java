package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.SavedGroupModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SavedStopAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<SavedGroupModel> groups = new ArrayList<SavedGroupModel>();
    private final LayoutInflater layoutInflater;

    private OnClickListener clickListener;

    public interface OnClickListener {
        void onGroupExpanded(int groupPosition);
        void onGroupCollapsed(int groupPosition);
        void onStopClicked(SavedStopModel stop);
        void onGroupRemoved(int groupPosition);
        void onStopRemoved(int groupPosition, int childPosition);
    }

    public SavedStopAdapter(Context context, @NonNull Collection<SavedGroupModel> groups) {
        this.context = context;
        layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.groups = (List<SavedGroupModel>) groups;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_saved_stop, null);
        }

        final SavedStopModel stop = SavedStopUtils.getSavedStop(context, groupPosition, childPosition);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onStopClicked(stop);
                }
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.popupmenu_saved_stop_options, popup.getMenu());
                popup.setOnMenuItemClickListener(getPopupMenuClickListener(groupPosition, childPosition));
                popup.show();
                return true;
            }
        });

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_saved_stop_title);
        tvTitle.setText(stop.getName());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).getStops().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_saved_group, null);
        }

        final SavedGroupModel group = groups.get(groupPosition);

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                popup.getMenuInflater().inflate(R.menu.popupmenu_saved_group_options, popup.getMenu());
                popup.setOnMenuItemClickListener(getPopupMenuClickListener(groupPosition, -1));
                popup.show();
                return true;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    if (isExpanded) {
                        clickListener.onGroupCollapsed(groupPosition);
                    } else {
                        clickListener.onGroupExpanded(groupPosition);
                    }
                }
            }
        });

        TextView title = (TextView) convertView.findViewById(R.id.tv_saved_group_title);
        title.setText(group.getName());

        ImageView ivIndicator = (ImageView) convertView.findViewById(R.id.iv_saved_group_indicator);
        ivIndicator.setImageDrawable(context.getResources().getDrawable(
                isExpanded ?
                        R.drawable.ic_expand_up :
                        R.drawable.ic_expand_down));

        return convertView;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void setData(@NonNull ArrayList<SavedGroupModel> groups) {
        this.groups = groups;
    }

    public void setOnClickListener(@NonNull OnClickListener listener) {
        this.clickListener = listener;
    }

    private PopupMenu.OnMenuItemClickListener getPopupMenuClickListener(
            final int groupPosition, final int stopPosition) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.saved_stop_remove) {
                    if (clickListener != null && groupPosition != -1 && stopPosition != -1) {
                        clickListener.onStopRemoved(groupPosition, stopPosition);
                    }
                } else if (item.getItemId() == R.id.saved_group_remove) {
                    if (clickListener != null && groupPosition != -1) {
                        clickListener.onGroupRemoved(groupPosition);
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
