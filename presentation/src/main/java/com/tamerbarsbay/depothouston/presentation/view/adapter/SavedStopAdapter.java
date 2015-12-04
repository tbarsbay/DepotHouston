package com.tamerbarsbay.depothouston.presentation.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.SavedGroupModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;
import com.tamerbarsbay.depothouston.presentation.util.DrawableUtils;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;
import com.tamerbarsbay.depothouston.presentation.util.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedStopAdapter
        extends AbstractExpandableItemAdapter<SavedStopAdapter.GroupViewHolder, SavedStopAdapter.ChildViewHolder>
        implements ExpandableDraggableItemAdapter<SavedStopAdapter.GroupViewHolder, SavedStopAdapter.ChildViewHolder> {

    private interface Expandable extends ExpandableItemConstants {}
    private interface Draggable extends DraggableItemConstants {}

    private final RecyclerViewExpandableItemManager expandableItemManager;
    private SavedStopsListener savedStopsListener;
    private View.OnClickListener onItemClickListener; //TODO use our own onclicklistener system?
    private Context context;

    public interface SavedStopsListener {
        void onGroupItemRemoved(int groupPosition); //TODO implement somewhere below
        void onChildItemRemoved(int groupPosition, int childPosition); //TODO implement somewhere below
        void onItemViewClicked(View v, boolean pinned); //TODO probably not needed
    }

    public static abstract class BaseViewHolder extends AbstractDraggableItemViewHolder implements ExpandableItemViewHolder {
        //TODO temp variables

        @Bind(R.id.saved_stop_container)
        FrameLayout container;

        @Bind(R.id.saved_stop_drag_handle)
        public View dragHandle;

        @Bind(R.id.tv_saved_stop_title)
        public TextView title;

        private int expandStateFlags;

        public BaseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public int getExpandStateFlags() {
            return expandStateFlags;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            expandStateFlags = flag;
        }
    }

    public static class GroupViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_saved_stop_group_indicator)
        public ImageView ivIndicator;

        public GroupViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class ChildViewHolder extends BaseViewHolder {
        public ChildViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public SavedStopAdapter(@NonNull Context context,
                            @NonNull RecyclerViewExpandableItemManager itemManager) {
        Log.d("SavedStopsAdapter", "constructor"); //TODO temp
        this.context = context;
        expandableItemManager = itemManager;
        onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
        setHasStableIds(true);
    }

    private void onItemViewClick(View v) {
        if (savedStopsListener != null) {
            savedStopsListener.onItemViewClicked(v, true); //TODO why true pinned
            //TODO call this somewhere
        }
    }

    @Override
    public int getGroupCount() {
        return SavedStopUtils.getGroupCount(context);
    }

    @Override
    public int getChildCount(int position) {
        return SavedStopUtils.getChildCount(context, position);
    }

    @Override
    public long getGroupId(int position) {
        Log.d("SavedStopAdapter", "getGroupId(" + position +")"); //TODO temp
        return SavedStopUtils.getGroupId(position);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.d("SavedStopAdapter", "getChildId(" + childPosition +")"); //TODO temp
        return SavedStopUtils.getChildId(groupPosition, childPosition);
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_saved_stop_group, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_saved_stop, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, final int groupPosition, int viewType) {
        Log.d("SavedStopsAdapter", "onBindGroupView"); //TODO temp
        // group item
        final SavedGroupModel group = SavedStopUtils.getGroupByRank(context, groupPosition);
        if (group == null) {
            Log.d("SavedStopAdapter", "Group null"); //TODO temp
            return;
        }

        // set listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Log.d("SavedStopAdapter", "Clicked group: " + group.getName()); //TODO temp
            }
        });

        // set text
        holder.title.setText(group.getName());

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();
        final int expandState = holder.getExpandStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) ||
                ((expandState & Expandable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            final boolean isExpanded;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                //bgResId = R.drawable.bg_group_item_dragging_active_state;
                bgResId = R.color.red_500; //TODO temp

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.container.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                //bgResId = R.drawable.bg_group_item_dragging_state;
                bgResId = R.color.green_500; //TODO temp
            } else if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                //bgResId = R.drawable.bg_group_item_expanded_state;
                bgResId = R.color.blue_500; //TODO temp
            } else {
                //bgResId = R.drawable.bg_group_item_normal_state;
                bgResId = R.color.white; //TODO temp
            }

            if ((expandState & Expandable.STATE_FLAG_IS_EXPANDED) != 0) {
                isExpanded = true;
                holder.ivIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_up)); //TODO test
            } else {
                isExpanded = false;
                holder.ivIndicator.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_expand_down)); //TODO test
            }

            holder.container.setBackgroundResource(bgResId);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    if (isExpanded) {
                        Log.d("SavedStopAdapter", "Clicked expand");
                        expandableItemManager.collapseGroup(groupPosition); //TODO temp
                    } else {
                        Log.d("SavedStopAdapter", "Clicked expand");
                        expandableItemManager.expandGroup(groupPosition); //TODO temp
                    }
                }
            });


            //holder.ivIndicator.setExpandedState(isExpanded, animateIndicator); //TODO
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        Log.d("SavedStopsAdapter", "onBindChildView"); //TODO temp
        // child item
        final SavedStopModel stop = SavedStopUtils.getSavedStop(context, groupPosition, childPosition);
        if (stop == null) {
            Log.d("SavedStopAdapter", "Stop null"); //TODO temp
            return;
        }

        // set listeners
        // (if the item is *not pinned*, click event comes to the itemView)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open stop arrivals
                Log.d("SavedStopAdapter", "Clicked stop: " + stop.getName()); //TODO temp
            }
        });

        // set text
        holder.title.setText(stop.getName());

        final int dragState = holder.getDragStateFlags();

        if ((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                //bgResId = R.drawable.bg_item_dragging_active_state;
                bgResId = R.color.purple_500; //TODO temp

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.container.getForeground());
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                //bgResId = R.drawable.bg_item_dragging_state;
                bgResId = R.color.green_500; //TODO temp
            } else {
                //bgResId = R.drawable.bg_item_normal_state;
                bgResId = R.color.white; //TODO temp
            }

            holder.container.setBackgroundResource(bgResId);
        }
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return !ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckGroupCanStartDrag(GroupViewHolder holder, int groupPosition, int x, int y) {
        Log.d("SavedStopAdapter", "onCheckGroupCanStartDrag"); //TODO test
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public boolean onCheckChildCanStartDrag(ChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.container;
        final View dragHandleView = holder.dragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        Log.d("SavedStopAdapter", "onCheckChildCanStartDrag"); //TODO test
        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(GroupViewHolder holder, int groupPosition) {
        // no drag-sortable range specified
        //return null; //TODO test
        return new ItemDraggableRange(0, getGroupCount()); //TODO test
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(ChildViewHolder holder, int groupPosition, int childPosition) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
        SavedStopUtils.moveSavedGroup(fromGroupPosition, toGroupPosition);
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        SavedStopUtils.moveSavedStop(fromGroupPosition, fromChildPosition, toGroupPosition, toChildPosition);
    }

    public void setSavedStopsListener(@NonNull SavedStopsListener listener) {
        savedStopsListener = listener;
    }
}
