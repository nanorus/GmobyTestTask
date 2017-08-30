package com.example.nanorus.gmobytesttask.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DatabaseHelper extends SQLiteOpenHelper {

    private DatabaseContract mDatabaseContract;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "RoutesDatabase.db";

    @Inject
    public DatabaseHelper(Context context, DatabaseContract databaseContract) {
        super(context, DB_NAME, null, DB_VERSION);
        mDatabaseContract = databaseContract;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(mDatabaseContract.SQL_CREATE_TABLE_FROM_CITY);
        sqLiteDatabase.execSQL(mDatabaseContract.SQL_CREATE_TABLE_TO_CITY);
        sqLiteDatabase.execSQL(mDatabaseContract.SQL_CREATE_TABLE_ROUTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
