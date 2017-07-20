package com.example.nanorus.gmobytesttask.view.route_info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.presenter.route_info.RouteInfoActivityPresenter;

public class RouteInfoActivity extends AppCompatActivity implements IRouteInfoActivity {

    RouteInfoActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);


        Fragment routeInfoFragment = new RouteInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", getIntent().getIntExtra("id", 0));
        routeInfoFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frgmCont, routeInfoFragment);
        fragmentTransaction.commit();

    }



    @Override
    public IRouteInfoActivity getView() {
        return this;
    }

}
