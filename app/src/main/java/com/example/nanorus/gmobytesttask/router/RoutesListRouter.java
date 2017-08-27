package com.example.nanorus.gmobytesttask.router;

import android.content.Context;
import android.content.Intent;

import com.example.nanorus.gmobytesttask.view.profile.ProfileActivity;
import com.example.nanorus.gmobytesttask.view.route_info.RouteInfoActivity;

import javax.inject.Inject;

public class RoutesListRouter {

    @Inject
    public RoutesListRouter(){

    }

    public void navigateToRouteInfoActivity(Context context, int routeId){
        Intent intent = new Intent(context, RouteInfoActivity.class);
        intent.putExtra("id", routeId);
        context.startActivity(intent);
    }

    public void navigateToProfileActivity(Context context){
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

}
