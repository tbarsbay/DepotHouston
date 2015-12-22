package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.StopModel;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;
import com.tamerbarsbay.depothouston.presentation.view.DividerItemDecoration;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SavedStopAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedStopsFragment extends BaseFragment {
    //TODO implement SavedStopView?

    public interface SavedStopsListener {
        void onStopClicked(StopModel stopModel);
    }

    @Bind(R.id.rv_saved_stops)
    RecyclerView rvSavedStops;

    private SavedStopAdapter adapter;
    private RecyclerViewExpandableItemManager itemManager;
    private RecyclerViewDragDropManager dragDropManager;
    private RecyclerView.Adapter wrappedAdapter;
    private SavedStopsListener savedStopsListener;

    public SavedStopsFragment() {}

    public static SavedStopsFragment newInstance() {
        return new SavedStopsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_saved_stops, container, false);
        ButterKnife.bind(this, fragmentView);
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemManager = new RecyclerViewExpandableItemManager(savedInstanceState);
        dragDropManager = new RecyclerViewDragDropManager();

        adapter = new SavedStopAdapter(getContext(), itemManager);
        adapter.setSavedStopsListener(new SavedStopAdapter.SavedStopsListener() {
            @Override
            public void onGroupItemRemoved(int groupPosition) {
                //TODO
                Log.d("SavedStopsFragment", "onGroupItemRemoved: " + groupPosition);
            }

            @Override
            public void onChildItemRemoved(int groupPosition, int childPosition) {
                //TODO
                Log.d("SavedStopsFragment", "onChildItemRemoved: " + groupPosition);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                //TODO
                Log.d("SavedStopsFragment", "onItemViewClicked");
            }
        });

        wrappedAdapter = itemManager.createWrappedAdapter(adapter);
        wrappedAdapter = dragDropManager.createWrappedAdapter(wrappedAdapter);

        rvSavedStops.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSavedStops.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        rvSavedStops.setAdapter(wrappedAdapter);

        dragDropManager.attachRecyclerView(rvSavedStops);

        itemManager.expandAll();
    }

    @Override
    public void onDestroyView() {
        if (dragDropManager != null) {
            dragDropManager.release();
            dragDropManager = null;
        }

        if (itemManager != null) {
            itemManager.release();
            itemManager = null;
        }

        if (rvSavedStops != null) {
            rvSavedStops.setAdapter(null);
            rvSavedStops = null;
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter);
            wrappedAdapter = null;
        }
        adapter = null;

        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create_group) {
            showCreateGroupDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SavedStopsListener) {
            savedStopsListener = (SavedStopsListener) context;
        }
    }

    private void showCreateGroupDialog() {
        View dialogView = getLayoutInflater(null).inflate(R.layout.view_create_group, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();

        final EditText etGroupName = (EditText) dialogView.findViewById(R.id.et_new_group_name);
        final TextView tvDuplicateWarning = (TextView) dialogView.findViewById(R.id.tv_new_group_duplicate);
        final Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final Button btnCreate = (Button) dialogView.findViewById(R.id.btn_create);

        etGroupName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                if (text.length() == 0) {
                    tvDuplicateWarning.setVisibility(View.GONE);
                    btnCreate.setEnabled(false);
                } else {
                    if (SavedStopUtils.doesGroupExist(getContext(), text)) {
                        tvDuplicateWarning.setVisibility(View.VISIBLE);
                        btnCreate.setEnabled(false);
                    } else {
                        tvDuplicateWarning.setVisibility(View.GONE);
                        btnCreate.setEnabled(true);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = etGroupName.getText().toString().trim();
                createGroup(groupName);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void createGroup(String groupName) {
        SavedStopUtils.createGroup(getContext(), groupName);
        adapter.notifyItemInserted(itemManager.getGroupCount()-1);
    }
}
