package com.example.nanorus.gmobytesttask.presenter.route_info;

import com.example.nanorus.gmobytesttask.view.route_info.IRouteInfoFragment;

public interface IRouteInfoFragmentPresenter {

    void loadAndShowData( int id);

    void bindView(IRouteInfoFragment view);

    void releasePresenter();

}
