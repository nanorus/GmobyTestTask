package com.example.nanorus.gmobytesttask.view.routes_list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.presenter_base.BasePresenterFragment;
import com.example.nanorus.gmobytesttask.presenter_base.PresenterFactory;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenterFactory;
import com.example.nanorus.gmobytesttask.view.ui.RecyclerViewItemClickSupport;
import com.example.nanorus.gmobytesttask.view.ui.adapter.RoutesListAdapter;

import java.util.ArrayList;
import java.util.List;


public class RoutesListFragment extends BasePresenterFragment<RoutesListFragmentPresenter, IRoutesListFragment> implements IRoutesListFragment {
    private static final String TAG = "RoutesListFragment";

    RoutesListFragmentPresenter mPresenter;

    RoutesListAdapter mAdapter;
    LinearLayoutManager mManager;
    List<RouteMainInfoPojo> mData;

    RecyclerView fragment_routes_list_rv_list;
    TextView fragment_routes_list_tv_no_data;

    RoutesListEventListener mRoutesListEventListener;

    private int mListItemClickedPosition = 0;

    public interface RoutesListEventListener {

        void showAlertLoadFail(String message);

        void showAlert(String message);

        void hideAlert();

    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mRoutesListEventListener = (RoutesListEventListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated-" + tag());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume-" + tag());
        Log.d(TAG, "onResume- is_presenter_null:" + String.valueOf(mPresenter == null));
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause-" + tag());
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop-" + tag());
        /*
        mPresenter.releasePresenter();
        */
        super.onStop();
    }

    @NonNull
    @Override
    protected String tag() {
        return "sample tag";
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
        Log.d(TAG, "onStart-" + tag());
        // When first created the Fragment, the Presenter will be initialized at this point, but on
        // a configuration change it wont be ready until onResume
        Log.d(TAG, "onStart- is_presenter_null:" + String.valueOf(mPresenter == null));

        /*
        mPresenter = new RoutesListFragmentPresenter(
                getViewLayer()
        );
        */
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes_list, container, false);

        fragment_routes_list_rv_list = (RecyclerView) v.findViewById(R.id.fragment_routes_list_rv_list);
        fragment_routes_list_tv_no_data = (TextView) v.findViewById(R.id.fragment_routes_list_tv_no_data);
        hideNoDataText();


        RecyclerViewItemClickSupport.addTo(fragment_routes_list_rv_list).setOnItemClickListener((recyclerView, position, v1) -> {
            mListItemClickedPosition = position;
            mPresenter.onListItemClicked();
        });

        return v;
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
        mRoutesListEventListener.showAlertLoadFail(this.getString(R.string.no_internet));
    }

    @Override
    public void showAlertServerError() {
        mRoutesListEventListener.showAlertLoadFail(this.getString(R.string.server_error));
    }

    @Override
    public void showAlertLoading() {
        mRoutesListEventListener.showAlert("Загрузка данных");
    }

    @Override
    public void showAlertInsert() {
        mRoutesListEventListener.showAlert("Сохранение данных");
    }

    @Override
    public void hideAlert() {
        mRoutesListEventListener.hideAlert();
    }


    @Override
    public int getListItemsCount() {
        return mData.size();
    }

    @Override
    public RouteMainInfoPojo getDataByListPosition(int position) {
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
