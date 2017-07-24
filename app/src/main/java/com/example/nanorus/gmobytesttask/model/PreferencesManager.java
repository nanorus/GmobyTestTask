package com.example.nanorus.gmobytesttask.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.nanorus.gmobytesttask.app.App;

public class PreferencesManager {

    private static SharedPreferences sInstance = null;

    public static SharedPreferences getPreferences() {
        if (sInstance == null) {
            sInstance = PreferenceManager.getDefaultSharedPreferences(App.getApp());
        }
        return sInstance;
    }

    public static void setIsIntoDBInserting(boolean isIntoDBInserting) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean("is_into_db_inserting", isIntoDBInserting);
        editor.apply();
    }

    public static boolean getIsIntoDBInserting() {
        return getPreferences().getBoolean("is_into_db_inserting", false);
    }

}
