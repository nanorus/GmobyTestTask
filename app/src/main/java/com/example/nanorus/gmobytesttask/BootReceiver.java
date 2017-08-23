package com.example.nanorus.gmobytesttask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.nanorus.gmobytesttask.view.routes_list.RoutesListActivity;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intentActivity = new Intent(context, RoutesListActivity.class);
            intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentActivity);
        }
    }
}
