package com.example.nanorus.gmobytesttask.view.route_info;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoFragmentPresenter;


public class RouteInfoFragment extends Fragment implements IRouteInfoFragment{

    private RouteInfoFragmentPresenter mPresetner;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_route_info, container, false);

        mPresetner = new RouteInfoFragmentPresenter(this, this.getArguments().getInt("id"));

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public IRouteInfoFragment getViewLayer() {
        return this;
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
