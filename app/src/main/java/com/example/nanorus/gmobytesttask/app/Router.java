package com.example.nanorus.gmobytesttask.app;

import android.content.Context;
import android.content.Intent;

import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoActivity;

public class Router {

    public static void navigateToRouteInfoActivity(Context context, int routeId){
        Intent intent = new Intent(context, RouteInfoActivity.class);
        intent.putExtra("id", routeId);
        context.startActivity(intent);
    }

}
