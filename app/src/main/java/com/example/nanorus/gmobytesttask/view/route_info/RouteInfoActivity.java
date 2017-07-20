package com.example.nanorus.gmobytesttask.view.route_info;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.nanorus.gmobytesttask.R;

public class RouteInfoActivity extends AppCompatActivity implements IRouteInfoActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_info);
    }

    @Override
    public IRouteInfoActivity getView() {
        return this;
    }

}
