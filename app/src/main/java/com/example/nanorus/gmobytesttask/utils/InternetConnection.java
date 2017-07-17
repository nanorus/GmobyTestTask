package com.example.nanorus.gmobytesttask.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;

public class InternetConnection {
    public static boolean isOnline(@NonNull Context context) {
        return ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
}
