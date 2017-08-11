package com.example.nanorus.gmobytesttask.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InternetConnection {

    private Context mContext;

    @Inject
    public InternetConnection(Context context) {
        mContext = context;
    }

    public boolean isOnline() {
        return ((ConnectivityManager) mContext.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
