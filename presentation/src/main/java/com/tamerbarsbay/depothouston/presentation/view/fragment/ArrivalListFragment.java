package com.tamerbarsbay.depothouston.presentation.view.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.tamerbarsbay.depothouston.R;
import com.tamerbarsbay.depothouston.presentation.internal.di.components.ArrivalComponent;
import com.tamerbarsbay.depothouston.presentation.model.ArrivalModel;
import com.tamerbarsbay.depothouston.presentation.model.SavedStopModel;
import com.tamerbarsbay.depothouston.presentation.presenter.ArrivalListPresenter;
import com.tamerbarsbay.depothouston.presentation.util.SavedStopUtils;
import com.tamerbarsbay.depothouston.presentation.view.ArrivalListView;
import com.tamerbarsbay.depothouston.presentation.view.adapter.ArrivalListAdapter;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArrivalListFragment extends BaseFragment implements ArrivalListView {

    public interface ArrivalListListener {
        void onArrivalClicked(ArrivalModel arrivalModel);
    }

    @Inject
    ArrivalListPresenter arrivalListPresenter;

    @Bind(R.id.rv_arrival_list)
    RecyclerView rvArrivals;

//    @Bind(R.id.tv_arrival_list_last_updated)
//    TextView tvLastUpdated;

    @Bind(R.id.ib_arrival_list_favorite)
    ImageButton ibFavorite;

    @Bind(R.id.ib_arrival_list_map)
    ImageButton ibViewMap;

    @Bind(R.id.ib_arrival_list_notifications)
    ImageButton ibNotification;

    @Bind(R.id.layout_progress)
    RelativeLayout rlProgress;

    @Bind(R.id.rl_retry)
    RelativeLayout rlRetry;

    @Bind(R.id.btn_retry)
    Button btnRetry;

    @Bind(R.id.layout_save_stop)
    LinearLayout layoutSaveStop;

    @Bind(R.id.layout_save_stop_new_group)
    LinearLayout layoutCreateNewGroup;

    @Bind(R.id.sp_save_stop_groups)
    Spinner spGroups;

    @Bind(R.id.et_save_stop_new_group_name)
    EditText etNewGroupName;

    @Bind(R.id.btn_save_stop_cancel)
    Button btnCancel;

    @Bind(R.id.btn_save_stop_save)
    Button btnSave;

    private ArrivalListAdapter arrivalsAdapter;
    private LinearLayoutManager arrivalsLayoutManager;

    private ArrivalListListener arrivalListListener;

    private static final String ARGUMENT_KEY_STOP_ID = "com.tamerbarsbay.depothouston.ARGUMENT_STOP_ID";
    private static final String ARGUMENT_KEY_STOP_NAME = "com.tamerbarsbay.depothouston.ARGUMENT_STOP_NAME";
    private String stopId;
    private String stopName;

    /**
     * When the user wants to favorite a stop, they are presented with a list of groups they can
     * put the stop into. This String is used as an item in the list in case the user wants to
     * create a new group to put the stop into.
     */
    private static final String CREATE_NEW_GROUP = "+ New Group";

    public ArrivalListFragment() {}

    /**
     * Create a new instance of an ArrivalListFragment using the id of the stop for which
     * to pull arrivals.
     * @param stopId The id of the stop for which we are loading arrivals.
     * @return New ArrivalListFragment object.
     */
    public static ArrivalListFragment newInstance(String stopId, String stopName) {
        ArrivalListFragment fragment = new ArrivalListFragment();
        Bundle args = new Bundle();
        args.putString(ARGUMENT_KEY_STOP_ID, stopId);
        args.putString(ARGUMENT_KEY_STOP_NAME, stopName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ArrivalListListener) {
            this.arrivalListListener = (ArrivalListListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_arrival_list, container, false);
        ButterKnife.bind(this, fragmentView);

        arrivalsLayoutManager = new LinearLayoutManager(getContext());
        rvArrivals.setLayoutManager(arrivalsLayoutManager);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
        loadArrivalList();
    }

    @Override
    public void onResume() {
        super.onResume();
        arrivalListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        arrivalListPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arrivalListPresenter.destroy();
    }

    private void initialize() {
        getComponent(ArrivalComponent.class).inject(this);
        arrivalListPresenter.setView(this);
        if (getArguments() != null) {
            stopId = getArguments().getString(ARGUMENT_KEY_STOP_ID, null);
            stopName = getArguments().getString(ARGUMENT_KEY_STOP_NAME, null);

            if (stopId != null) {
                setFavoriteIcon();
            }

//            //TODO TEMPORARY - REMOVE
//            Log.d("ArrivalListFragment", "Saving stop: " + stopName);
//            SavedStopUtils.saveStopToGroup(getContext(), "Test Group 2", new SavedStopModel(0, stopId, stopName)); //TODO 0 id temp
        }
    }

    @Override
    public void renderArrivalList(Collection<ArrivalModel> arrivalModels) {
        if (arrivalModels != null) {
            if (this.arrivalsAdapter == null) {
                this.arrivalsAdapter = new ArrivalListAdapter(getActivity(), arrivalModels);
            } else {
                this.arrivalsAdapter.setArrivalsCollection(arrivalModels);
            }
            this.arrivalsAdapter.setOnItemClickListener(onItemClickListener);
            this.rvArrivals.setAdapter(arrivalsAdapter);
        }
    }

    /**
     * Set the favorite icon to a hollow star if this stop is not saved yet,
     * or a full star if it is.
     */
    private void setFavoriteIcon() {
        if (stopId == null) {
            return;
        }
        if (SavedStopUtils.isStopSaved(getContext(), stopId)) {
            ibFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
        } else {
            ibFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
        }
    }

    @Override
    public void showLoadingView() {
        this.rlProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        this.rlProgress.setVisibility(View.GONE);
    }

    @Override
    public void showRetryView() {
        this.rlRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetryView() {
        this.rlRetry.setVisibility(View.GONE);
    }

    @Override public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    private void loadArrivalList() {
        this.arrivalListPresenter.initialize();
    }

    private void populateSavedGroupsSpinner() {
        if (spGroups != null) {
            ArrayList<String> groupNames = SavedStopUtils.getGroupNamesArray(getContext());
            groupNames.add(CREATE_NEW_GROUP);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), R.layout.list_item_simple, R.id.tv_simple_list_item_name, groupNames);
            spGroups.setAdapter(adapter);
        }
    }

    private void hideSaveMenu() {
        if (layoutSaveStop.getVisibility() == View.VISIBLE) {
            layoutSaveStop.setVisibility(View.GONE);
        }
    }

    private void showSaveMenu() {
        if (layoutSaveStop.getVisibility() == View.GONE) {
            layoutSaveStop.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_retry)
    void onButtonRetryClick() {
        ArrivalListFragment.this.loadArrivalList();
    }

    @OnClick(R.id.ib_arrival_list_favorite)
    void onFavoriteIconClicked() {
        if (layoutSaveStop.getVisibility() == View.GONE) {
            showSaveMenu();
            populateSavedGroupsSpinner();
        }
    }

    @OnClick(R.id.btn_save_stop_save)
    void onSaveStopClicked() {
        String groupName;
        if (spGroups.getSelectedItem().toString().equals(CREATE_NEW_GROUP)) {
            // User is creating new group
            groupName = etNewGroupName.getText().toString().trim();
        } else {
            // User is adding to existing group
            groupName = spGroups.getSelectedItem().toString();
        }

        if (groupName.length() > 0 && stopId != null && stopName != null) {
            btnSave.setEnabled(false);
            SavedStopUtils.saveStopToGroup(
                    getContext(),
                    groupName,
                    new SavedStopModel(0, stopId, stopName));
            hideSaveMenu();
            showSnackbar(getView(), "This stop has been saved to your favorites!");
        }
    }

    @OnClick(R.id.btn_save_stop_cancel)
    void onCancelSaveStopClicked() {
        spGroups.setSelection(0);
        hideSaveMenu();
    }

    @OnItemSelected(R.id.sp_save_stop_groups)
    void onSavedGroupSelected(int position) {
        int lastSpinnerPosition = spGroups.getAdapter().getCount()-1;
        if (position == lastSpinnerPosition) {
            etNewGroupName.clearComposingText();
            btnSave.setEnabled(false);
            layoutCreateNewGroup.setVisibility(View.VISIBLE);
        } else {
            layoutCreateNewGroup.setVisibility(View.GONE);
            etNewGroupName.clearComposingText();
            btnSave.setEnabled(true);
        }
    }

    @OnTextChanged(R.id.et_save_stop_new_group_name)
    void onNewGroupNameTextChanged(CharSequence text) {
        btnSave.setEnabled(text.toString().trim().length() > 0);
    }

    private ArrivalListAdapter.OnItemClickListener onItemClickListener =
            new ArrivalListAdapter.OnItemClickListener() {
                @Override
                public void onArrivalItemClicked(ArrivalModel arrivalModel) {
                    if (ArrivalListFragment.this.arrivalListPresenter != null && arrivalModel != null) {
                        //ArrivalListFragment.this.arrivalListPresenter.onArrivalClicked(arrivalModel);
                        //TODO necessary?
                    }
                }
            };

}
