package com.example.nanorus.gmobytesttask.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nanorus.gmobytesttask.app.App;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.FromCityPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.ToCityPojo;

import rx.Observable;

public class DatabaseManager {

    private static DatabaseHelper sDatabaseHelper = null;

    public static DatabaseHelper getDatabaseHelper() {
        if (sDatabaseHelper == null) {
            sDatabaseHelper = new DatabaseHelper(App.getApp().getApplicationContext());
        }
        return sDatabaseHelper;
    }

    public static void putRoutes(RequestPojo routesFullInfo) {


                try {
                    DatabaseHelper databaseHelper = getDatabaseHelper();
                    SQLiteDatabase database = databaseHelper.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    DatumPojo datumPojo;
                    FromCityPojo fromCityPojo;
                    ToCityPojo toCityPojo;

                    // insert to From City table
                    for (int i = 0; i < routesFullInfo.getData().size(); i++) {
                        fromCityPojo = routesFullInfo.getData().get(i).getFromCity();

                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_ID, fromCityPojo.getId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_HIGHLIGHT, fromCityPojo.getHighlight());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_NAME, fromCityPojo.getName());

                        database.insertWithOnConflict(DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY,
                                null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                        contentValues.clear();
                    }
                    fromCityPojo = null;

                    // insert to To City table
                    for (int i = 0; i < routesFullInfo.getData().size(); i++) {
                        toCityPojo = routesFullInfo.getData().get(i).getToCity();

                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_ID, toCityPojo.getId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_HIGHLIGHT, toCityPojo.getHighlight());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_NAME, toCityPojo.getName());


                        database.insertWithOnConflict(DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY,
                                null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                        contentValues.clear();
                    }
                    toCityPojo = null;

                    // insert to Routes table
                    for (int i = 0; i < routesFullInfo.getData().size(); i++) {
                        datumPojo = routesFullInfo.getData().get(i);

                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_ID, datumPojo.getId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_CITY, datumPojo.getFromCity().getId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_CITY, datumPojo.getToCity().getId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE, datumPojo.getFromDate());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE, datumPojo.getToDate());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_PRICE, datumPojo.getPrice());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_INFO, datumPojo.getFromInfo());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_INFO, datumPojo.getToInfo());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_INFO, datumPojo.getInfo());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_BUS_ID, datumPojo.getBusId());
                        contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_RESERVATION_COUNT, datumPojo.getReservationCount());

                        database.insertWithOnConflict(DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES,
                                null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                        contentValues.clear();
                    }
                    datumPojo = null;

                    // database = null;
                    // super.run();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


    }

    public static Observable<RouteMainInfoPojo> getRoutesMainInfo(int fromDate, int toDate) {
        Observable<RouteMainInfoPojo> routesMainInfo = Observable.create(
                subscriber -> {
                    DatabaseHelper helper = DatabaseManager.getDatabaseHelper();
                    SQLiteDatabase database = helper.getReadableDatabase();

                    String tableNameRoutes = DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES;
                    String tableNameFromCity = DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY;
                    String tableNameToCity = DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY;

                    // define names of columns
                    String columnIdName = tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_ID;
                    String columnFromDateName = tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE;
                    String columnToDateName = tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE;
                    String columnPriceName = tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_PRICE;
                    String columnFromCityName = tableNameFromCity + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_NAME;
                    String columnToCityName = tableNameToCity + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_NAME;

                    String comma = ",";

                    Cursor cursor = database.rawQuery("SELECT " +
                                    columnIdName + " AS Id" + comma +
                                    columnFromDateName + " AS FromDate" + comma +
                                    columnToDateName + " AS ToDate" + comma +
                                    columnPriceName + " AS Price" + comma +
                                    columnFromCityName + " AS FromCity" + comma +
                                    columnToCityName + " AS ToCity" +

                                    " FROM " + DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES +

                                    " INNER JOIN " + tableNameFromCity + " ON " +
                                    tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_CITY + " = " +
                                    tableNameFromCity + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_ID +

                                    " INNER JOIN " + tableNameToCity + " ON " +
                                    tableNameRoutes + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_CITY + " = " +
                                    tableNameToCity + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_ID
                            , null);
                    if (cursor.moveToFirst()) {
                        do {
                            subscriber.onNext(new RouteMainInfoPojo(
                                    cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("FromCity")),
                                    cursor.getString(cursor.getColumnIndex("ToCity")),
                                    cursor.getString(cursor.getColumnIndex("FromDate")),
                                    cursor.getString(cursor.getColumnIndex("ToDate")),
                                    cursor.getInt(cursor.getColumnIndex("Price"))
                            ));
                        }
                        while (cursor.moveToNext());
                    }
                    subscriber.onCompleted();
                    //   database.close();
                });
        return routesMainInfo;
    }

    @Override
    protected void finalize() throws Throwable {
        DatabaseManager.getDatabaseHelper().close();
        super.finalize();
    }

    public static Observable<DatumPojo> getRouteFullInfo(int routeId) {
        return null;
    }

    public static void cleanSavedRoutes() {
        DatabaseHelper helper = getDatabaseHelper();
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES, null, null);
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY, null, null);
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY, null, null);
    }

}
