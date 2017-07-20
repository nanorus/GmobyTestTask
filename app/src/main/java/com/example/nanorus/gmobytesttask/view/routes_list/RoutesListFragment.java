package com.example.nanorus.gmobytesttask.view.routes_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.view.ui.adapter.RoutesListAdapter;

import java.util.ArrayList;
import java.util.List;


public class RoutesListFragment extends Fragment implements IRoutesListFragment {

    RoutesListFragmentPresenter mPresenter;

    RoutesListAdapter mAdapter;
    LinearLayoutManager mManager;
    ArrayList<RouteMainInfoPojo> mData;

    RecyclerView fragment_routes_list_rv_list;
    TextView fragment_routes_list_tv_no_data;

    OnSnackBarEventListener mSnackBarEventListener;

    public interface OnSnackBarEventListener {
        void showSnackBar(String message, int duration);
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            mSnackBarEventListener = (OnSnackBarEventListener) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        try {
            mSnackBarEventListener = (OnSnackBarEventListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        mPresenter.releasePresenter();
        mPresenter = null;
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes_list, container, false);

        fragment_routes_list_rv_list = (RecyclerView) v.findViewById(R.id.fragment_routes_list_rv_list);
        fragment_routes_list_tv_no_data = (TextView) v.findViewById(R.id.fragment_routes_list_tv_no_data);
        hideNoDataText();

        mPresenter = new RoutesListFragmentPresenter(getViewLayer());

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
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void updateAdapter(List<RouteMainInfoPojo> newData) {
        mData.clear();
        mData.addAll(newData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSnackBarNoInternet() {
        mSnackBarEventListener.showSnackBar(App.getApp().getString(R.string.no_internet), Snackbar.LENGTH_LONG);
    }

    @Override
    public void showSnackBarServerError() {
        mSnackBarEventListener.showSnackBar(App.getApp().getString(R.string.server_error), Snackbar.LENGTH_LONG);

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
    public int getListItemsCount() {
        System.out.println("list items count: " + mData.size());
        return mData.size();
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
