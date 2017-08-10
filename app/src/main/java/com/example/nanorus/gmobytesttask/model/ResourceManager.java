package com.example.nanorus.gmobytesttask.model;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourceManager {

    Context mContext;

    @Inject
    public ResourceManager(Context context) {
        mContext = context;
    }

    public String getString(int stringInt) {
        String string;
        if (mContext != null) {
            string = mContext.getString(stringInt);
        } else
            string = "none";
        return string;
    }

}
