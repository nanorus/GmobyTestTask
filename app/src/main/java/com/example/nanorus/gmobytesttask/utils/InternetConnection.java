package com.example.nanorus.gmobytesttask.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import com.example.nanorus.gmobytesttask.app.App;

public class InternetConnection {
    public static boolean isOnline() {
        return ((ConnectivityManager) App.getApp().getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
