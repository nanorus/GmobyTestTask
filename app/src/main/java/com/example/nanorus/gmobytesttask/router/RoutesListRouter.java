package com.example.nanorus.gmobytesttask.router;

import android.content.Context;
import android.content.Intent;

import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoActivity;

public class RoutesListRouter {

    public void navigateToRouteInfoActivity(Context context, int routeId){
        Intent intent = new Intent(context, RouteInfoActivity.class);
        intent.putExtra("id", routeId);
        context.startActivity(intent);
    }
}
