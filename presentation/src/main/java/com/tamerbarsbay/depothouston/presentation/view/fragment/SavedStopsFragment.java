package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopGroupModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;
import com.tamerbarsbay.depothouston.presentation.view.adapter.SavedStopAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedStopsFragment extends BaseFragment {
    //TODO implement SavedStopView?

    public interface SavedStopsListener {
        void onStopClicked(SavedStopModel stop);
    }

    @Bind(R.id.elv_saved_stops)
    ExpandableListView elvSavedStops;

    private SavedStopAdapter adapter;
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

        ArrayList<SavedStopGroupModel> groups = SavedStopUtils.getSavedStopGroups(getContext());
        adapter = new SavedStopAdapter(getContext(), groups);
        adapter.setOnClickListener(onClickListener);

        elvSavedStops.setAdapter(adapter);
        for (int i = 0; i < groups.size(); i++) {
            elvSavedStops.expandGroup(i, true);
        }
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
        updateData();
    }

    private void updateData() {
        if (adapter != null) {
            adapter.setData(SavedStopUtils.getSavedStopGroups(getContext()));
            adapter.notifyDataSetChanged();
        }
    }

    private SavedStopAdapter.OnClickListener onClickListener =
            new SavedStopAdapter.OnClickListener() {
                @Override
                public void onGroupExpanded(int groupPosition) {
                    elvSavedStops.expandGroup(groupPosition);
                }

                @Override
                public void onGroupCollapsed(int groupPosition) {
                    elvSavedStops.collapseGroup(groupPosition);
                }

                @Override
                public void onGroupRemoved(int groupPosition) {
                    SavedStopUtils.removeGroup(getContext(), groupPosition);
                    updateData();
                }

                @Override
                public void onStopRemoved(int groupPosition, int stopPosition) {
                    SavedStopUtils.removeStop(getContext(), groupPosition, stopPosition);
                    updateData();
                }

                @Override
                public void onStopClicked(SavedStopModel stop) {
                    if (savedStopsListener != null) {
                        savedStopsListener.onStopClicked(stop);
                    }
                }
            };
}
