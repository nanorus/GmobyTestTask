package com.example.nanorus.gmobytesttask.view.routes_list;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.presenter.routes_list.RoutesListFragmentPresenter;
import com.example.nanorus.gmobytesttask.view.ui.RecyclerViewItemClickSupport;
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
    NestedScrollView fragment_routes_list_nested;

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
    public void onAttach(Context context) {
        try {
            mRoutesListEventListener = (RoutesListEventListener) context;
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
        fragment_routes_list_nested = (NestedScrollView) v.findViewById(R.id.fragment_routes_list_nested);
        fragment_routes_list_nested.post(() -> fragment_routes_list_nested.scrollTo(0, 0));

        mPresenter = new RoutesListFragmentPresenter(getViewLayer());

        RecyclerViewItemClickSupport.addTo(fragment_routes_list_rv_list).setOnItemClickListener((recyclerView, position, v1) -> {
            mListItemClickedPosition = position;
            mPresenter.onListItemClicked();
        });

        ViewCompat.setNestedScrollingEnabled(fragment_routes_list_rv_list, false);

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
