package com.example.nanorus.gmobytesttask.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "RoutesDatabase2.db";

    @Inject
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DatabaseContract.DatabaseEntry.SQL_CREATE_TABLE_FROM_CITY);
        sqLiteDatabase.execSQL(DatabaseContract.DatabaseEntry.SQL_CREATE_TABLE_TO_CITY);
        sqLiteDatabase.execSQL(DatabaseContract.DatabaseEntry.SQL_CREATE_TABLE_ROUTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
