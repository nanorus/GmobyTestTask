package com.example.nanorus.gmobytesttask.view.routes_list;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.base.BasePresenterFragment;
import com.example.nanorus.gmobytesttask.presenter.base.PresenterFactory;
import com.example.nanorus.gmobytesttask.presenter.routes_list.IRoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenterFactory;
import com.example.nanorus.gmobytesttask.view.ui.RecyclerViewItemClickSupport;
import com.example.nanorus.gmobytesttask.view.ui.adapter.RoutesListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RoutesListFragment extends BasePresenterFragment<RoutesListFragmentPresenter, IRoutesListFragment> implements IRoutesListFragment {


    private IRoutesListFragmentPresenter mPresenter;

    private RoutesListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private List<RouteMainInfoPojo> mData;
    private String mFromDate;
    private String mToDate;

    private RecyclerView fragment_routes_list_rv_list;
    private TextView fragment_routes_list_tv_no_data;
    private TextView mTvPeriod;


    private RoutesListEventListener mActivityEventListener;

    private int mListItemClickedPosition = 0;

    public void updateListOnline() {
        // mPresenter.updateListOnline();
        mPresenter.updateListOnlineService();
    }

    public void updateListOffline() {
        mPresenter.updateListOffline();
    }

    public interface RoutesListEventListener {

        void showAlert(String message);

        void hideAlert();

        void showAlertRetryOnlineLoading(String message);

        void showSwipeRefreshing(boolean willShow);

        void showRouteInfoByPosition(int position);

        boolean isInfoFragmentExist();

    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mActivityEventListener = (RoutesListEventListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @NonNull
    @Override
    protected String tag() {
        return "sample tag";
    }

    public RouteMainInfoPojo getRouteByPosition(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    protected PresenterFactory<RoutesListFragmentPresenter> getPresenterFactory() {
        return new RoutesListFragmentPresenterFactory();
    }

    @Override
    protected void onPresenterCreatedOrRestored(@NonNull RoutesListFragmentPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes_list, container, false);

        fragment_routes_list_rv_list = (RecyclerView) v.findViewById(R.id.fragment_routes_list_rv_list);
        fragment_routes_list_tv_no_data = (TextView) v.findViewById(R.id.fragment_routes_list_tv_no_data);
        mTvPeriod = (TextView) v.findViewById(R.id.fragment_routes_list_tv_period);


        mFromDate = getArguments().getString("from_date");
        mToDate = getArguments().getString("to_date");
        setPeriodTextByApiFormat(mFromDate, mToDate);
        hideNoDataText();

        RecyclerViewItemClickSupport.addTo(fragment_routes_list_rv_list).setOnItemClickListener((recyclerView, position, v1) -> {
            mListItemClickedPosition = position;
            if (mActivityEventListener.isInfoFragmentExist()
                    && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mActivityEventListener.showRouteInfoByPosition(position);
            } else
                mPresenter.onListItemClicked();
        });
        return v;
    }

    private void setPeriodTextByApiFormat(String fromDateApiFormat, String toDateApiFormat) {
        System.out.println("fragment: to date: " + toDateApiFormat);
        SimpleDateFormat materialFormat = new SimpleDateFormat("dd MMM yy", Locale.ENGLISH);
        if (fromDateApiFormat != null && toDateApiFormat != null) {

            Date fromDate = new Date(
                    Integer.parseInt(fromDateApiFormat.substring(0, 4)),
                    Integer.parseInt(fromDateApiFormat.substring(4, 6)) - 1,
                    Integer.parseInt(fromDateApiFormat.substring(6, 8)));
            Date toDate = new Date(
                    Integer.parseInt(toDateApiFormat.substring(0, 4)),
                    Integer.parseInt(toDateApiFormat.substring(4, 6)) - 1,
                    Integer.parseInt(toDateApiFormat.substring(6, 8)));
            mTvPeriod.setText(materialFormat.format(fromDate) + " - " + materialFormat.format(toDate));
        }
    }

    @Override
    public void createAndSetAdapter() {
        mData = new ArrayList<>();
        mAdapter = new RoutesListAdapter(mData);
        mManager = new LinearLayoutManager(getViewLayer().getViewContext(), LinearLayoutManager.VERTICAL, false);
        fragment_routes_list_rv_list.setLayoutManager(mManager);
        fragment_routes_list_rv_list.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(fragment_routes_list_rv_list.getContext(),
                mManager.getOrientation());
        fragment_routes_list_rv_list.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void addDataToListAndUpdateAdapter(RouteMainInfoPojo routeMainInfoPojo) {
        mData.add(routeMainInfoPojo);
        mAdapter.notifyItemInserted(mData.size());
    }


    @Override
    public void fullUpdateAdapter(List<RouteMainInfoPojo> newData) {
        mData.clear();
        mData.addAll(newData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public List<RouteMainInfoPojo> getData() {
        return mData;
    }

    @Override
    public void showNoDataText() {
        fragment_routes_list_tv_no_data.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoDataText() {
        fragment_routes_list_tv_no_data.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showAlertNoInternet() {
        mActivityEventListener.showAlertRetryOnlineLoading(this.getString(R.string.no_internet));
    }

    @Override
    public void showSwipeRefreshing(boolean willShow) {
        mActivityEventListener.showSwipeRefreshing(willShow);
    }


    @Override
    public void showAlertRetryOnlineLoading(String message) {
        mActivityEventListener.showAlertRetryOnlineLoading(message);
    }


    @Override
    public void showAlertLoading() {
        mActivityEventListener.showAlert("Загрузка данных");
    }

    @Override
    public void showAlertInsert() {
        mActivityEventListener.showAlert("Сохранение данных");
    }

    @Override
    public void hideAlert() {
        mActivityEventListener.hideAlert();
    }


    @Override
    public int getListItemsCount() {
        return mData.size();
    }

    @Override
    public int[] getDates() {
        return new int[]{Integer.parseInt(mFromDate), Integer.parseInt(mToDate)};
    }

    @Override
    public void showAlertFailLoading() {
        mActivityEventListener.showAlertRetryOnlineLoading("Загрузка не успешна");
    }

    @Override
    public RouteMainInfoPojo getDataByPositionAtList(int position) {
        return mData.get(position);
    }

    @Override
    public int getListItemClickedPosition() {
        return mListItemClickedPosition;
    }

    @Override
    public IRoutesListFragment getViewLayer() {
        return this;
    }

    @Override
    public Context getViewContext() {
        return this.getActivity();
    }

}
