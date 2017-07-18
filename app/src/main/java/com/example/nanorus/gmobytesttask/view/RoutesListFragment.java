package com.example.nanorus.gmobytesttask.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.RoutesPresenter;
import com.example.nanorus.gmobytesttask.view.ui.adapter.RoutesListAdapter;

import java.util.ArrayList;
import java.util.List;


public class RoutesListFragment extends Fragment implements IRoutesListFragment {

    RoutesPresenter mPresenter;

    RoutesListAdapter mAdapter;
    LinearLayoutManager mManager;
    ArrayList<RouteMainInfoPojo> mData;

    RecyclerView fragment_routes_list_rv_list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_routes_list, container, false);

        fragment_routes_list_rv_list = (RecyclerView) v.findViewById(R.id.fragment_routes_list_rv_list);

        mPresenter = new RoutesPresenter(getViewLayer());

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
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
    public void updateAdapter(List<RouteMainInfoPojo> newData) {
        mData.clear();
        mData.addAll(newData);
        mAdapter.notifyDataSetChanged();
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
