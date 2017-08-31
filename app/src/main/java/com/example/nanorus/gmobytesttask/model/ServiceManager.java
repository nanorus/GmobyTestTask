package com.example.nanorus.gmobytesttask.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nanorus.gmobytesttask.model.services.DownloadRoutesListService;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class ServiceManager {

    Context mContext;
    BroadcastReceiver mBroadcastReceiver;

    @Inject
    public ServiceManager(Context context) {
        mContext = context;

    }

    public Single<Boolean> loadRoutesList(String fromDate, String toDate) {
        return Single.create(singleSubscriber -> {
            mBroadcastReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    singleSubscriber.onSuccess(intent.getBooleanExtra("is_data_downloaded", false));
                }
            };

            IntentFilter intFilt = new IntentFilter(DownloadRoutesListService.BROADCAST_ACTION);
            LocalBroadcastManager.getInstance(mContext).registerReceiver(mBroadcastReceiver, intFilt);
            Intent intent = new Intent(new Intent(mContext, DownloadRoutesListService.class));
            intent.putExtra("from_date", fromDate);
            intent.putExtra("to_date", toDate);
            mContext.startService(intent);
        });
    }


}
