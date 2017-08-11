package com.example.nanorus.gmobytesttask.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nanorus.gmobytesttask.model.DataConverter;
import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.FromCityPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.ToCityPojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DatabaseManager {

    private DatabaseHelper mDatabaseHelper;

    @Inject
    DatabaseManager(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }


    private DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    // define names of tables and columns
    private final static String TABLE_NAME_ROUTES = DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES;
    private final static String TABLE_NAME_FROM_CITY = DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY;
    private final static String TABLE_NAME_TO_CITY = DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY;

    private final static String COLUMN_NAME_ROUTES_ID = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_ID;
    private final static String COLUMN_NAME_ROUTES_FROM_DATE = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE;
    private final static String COLUMN_NAME_ROUTES_TO_DATE = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE;
    private final static String COLUMN_NAME_ROUTES_FROM_INFO = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_INFO;
    private final static String COLUMN_NAME_ROUTES_TO_INFO = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_INFO;
    private final static String COLUMN_NAME_ROUTES_FROM_CITY = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_CITY;
    private final static String COLUMN_NAME_ROUTES_TO_CITY = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_CITY;
    private final static String COLUMN_NAME_ROUTES_PRICE = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_PRICE;
    private final static String COLUMN_NAME_ROUTES_INFO = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_INFO;
    private final static String COLUMN_NAME_ROUTES_BUS_ID = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_BUS_ID;
    private final static String COLUMN_NAME_ROUTES_RESERVATION_COUNT = TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_RESERVATION_COUNT;

    private final static String COLUMN_NAME_FROM_CITY_NAME = TABLE_NAME_FROM_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_NAME;
    private final static String COLUMN_NAME_FROM_CITY_ID = TABLE_NAME_FROM_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_ID;
    private final static String COLUMN_NAME_TO_CITY_NAME = TABLE_NAME_TO_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_NAME;
    private final static String COLUMN_NAME_FROM_CITY_HIGHLIGHT = TABLE_NAME_FROM_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_HIGHLIGHT;
    private final static String COLUMN_NAME_TO_CITY_ID = TABLE_NAME_TO_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_ID;
    private final static String COLUMN_NAME_TO_CITY_HIGHLIGHT = TABLE_NAME_TO_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_HIGHLIGHT;

    private final static String COMMA_SEP = ",";

    public void putRoutes(RequestPojo routesFullInfo) {
        try {
            SQLiteDatabase database = getDatabaseHelper().getWritableDatabase();

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
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE,
                        datumPojo.getFromDate() + " " + datumPojo.getFromTime());
                contentValues.put(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE,
                        datumPojo.getToDate() + " " + datumPojo.getToTime());
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

    public Observable<RouteMainInfoPojo> getRoutesMainInfo(int fromDate, int toDate) {
        return Observable.create(
                subscriber -> {
                    SQLiteDatabase database = getDatabaseHelper().getReadableDatabase();

                    Cursor cursor = database.rawQuery("SELECT " +
                                    COLUMN_NAME_ROUTES_ID + " AS Id" + COMMA_SEP +
                                    COLUMN_NAME_ROUTES_FROM_DATE + " AS FromDate" + COMMA_SEP +
                                    COLUMN_NAME_ROUTES_TO_DATE + " AS ToDate" + COMMA_SEP +
                                    COLUMN_NAME_ROUTES_PRICE + " AS Price" + COMMA_SEP +
                                    COLUMN_NAME_FROM_CITY_NAME + " AS FromCity" + COMMA_SEP +
                                    COLUMN_NAME_TO_CITY_NAME + " AS ToCity" +

                                    " FROM " + DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES +

                                    " INNER JOIN " + TABLE_NAME_FROM_CITY + " ON " +
                                    TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_CITY + " = " +
                                    TABLE_NAME_FROM_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_FROM_CITY_ID +

                                    " INNER JOIN " + TABLE_NAME_TO_CITY + " ON " +
                                    TABLE_NAME_ROUTES + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_CITY + " = " +
                                    TABLE_NAME_TO_CITY + "." + DatabaseContract.DatabaseEntry.COLUMN_NAME_TO_CITY_ID
                            , null);
                    if (cursor.moveToFirst()) {
                        do {
                            RouteMainInfoPojo routeMainInfoPojo = new RouteMainInfoPojo(
                                    cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("FromCity")),
                                    cursor.getString(cursor.getColumnIndex("ToCity")),
                                    DataConverter.convertApiDateFormatToCorrectDateFormat(cursor.getString(cursor.getColumnIndex("FromDate"))),
                                    DataConverter.convertApiDateFormatToCorrectDateFormat(cursor.getString(cursor.getColumnIndex("ToDate"))),
                                    cursor.getInt(cursor.getColumnIndex("Price"))
                            );
                            subscriber.onNext(routeMainInfoPojo);
                        }
                        while (cursor.moveToNext());
                    }
                    subscriber.onCompleted();
                    cursor.close();
                });
    }


    public Observable<DatumPojo> getRouteFullInfo(int routeId) {
        return Observable.create(subscriber -> {
            DatumPojo routeFullInfoPojo;

            DateFormat dateFormatDataBase = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat dateFormatPojo = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat timeFormatPojo = new SimpleDateFormat("HH:mm:ss");
            Date dateFrom = null;
            Date dateTo = null;

            int fromCityId = 0;
            int fromCityHighlight = 0;
            String fromCityName = null;
            int toCityId = 0;
            int toCityHighlight = 0;
            String toCityName = null;

            FromCityPojo fromCity = null;
            ToCityPojo toCity = null;

            String fromDate = null;
            String fromTime = null;
            String fromInfo = null;
            String toDate = null;
            String toTime = null;
            String toInfo = null;
            String info = null;
            int price = 0;
            int busId = 0;
            int reservationCount = 0;


            SQLiteDatabase database = getDatabaseHelper().getReadableDatabase();

            Cursor cursor = database.rawQuery(
                    "SELECT " +
                            COLUMN_NAME_ROUTES_ID + " AS routesId" + COMMA_SEP +
                            COLUMN_NAME_ROUTES_FROM_DATE + COMMA_SEP +
                            COLUMN_NAME_ROUTES_TO_DATE + COMMA_SEP +
                            COLUMN_NAME_ROUTES_FROM_INFO + COMMA_SEP +
                            COLUMN_NAME_ROUTES_TO_INFO + COMMA_SEP +
                            COLUMN_NAME_ROUTES_FROM_CITY + COMMA_SEP +
                            COLUMN_NAME_ROUTES_TO_CITY + COMMA_SEP +
                            COLUMN_NAME_ROUTES_PRICE + COMMA_SEP +
                            COLUMN_NAME_ROUTES_INFO + COMMA_SEP +
                            COLUMN_NAME_ROUTES_BUS_ID + COMMA_SEP +
                            COLUMN_NAME_ROUTES_RESERVATION_COUNT + COMMA_SEP +

                            COLUMN_NAME_FROM_CITY_ID + " AS fromCityId" + COMMA_SEP +
                            COLUMN_NAME_FROM_CITY_NAME + " AS fromCityName" + COMMA_SEP +
                            COLUMN_NAME_FROM_CITY_HIGHLIGHT + " AS fromCityHighLight" + COMMA_SEP +
                            COLUMN_NAME_TO_CITY_ID + " AS toCityId" + COMMA_SEP +
                            COLUMN_NAME_TO_CITY_NAME + " AS toCityName" + COMMA_SEP +
                            COLUMN_NAME_TO_CITY_HIGHLIGHT + " AS toCityHighLight" +

                            " FROM " + TABLE_NAME_ROUTES +

                            " INNER JOIN " + TABLE_NAME_FROM_CITY +
                            " ON fromCityId" +
                            " = " + COLUMN_NAME_ROUTES_FROM_CITY +

                            " INNER JOIN " + TABLE_NAME_TO_CITY +
                            " ON toCityId" +
                            " = " + COLUMN_NAME_ROUTES_TO_CITY +

                            " WHERE routesId =" + routeId
                            + " LIMIT 1"
                    , null);

            if (cursor.moveToFirst()) {
                do {
                    fromCityId = cursor.getInt(cursor.getColumnIndex("fromCityId"));
                    fromCityHighlight = cursor.getInt(cursor.getColumnIndex("fromCityHighLight"));
                    fromCityName = cursor.getString(cursor.getColumnIndex("fromCityName"));

                    toCityId = cursor.getInt(cursor.getColumnIndex("toCityId"));
                    toCityHighlight = cursor.getInt(cursor.getColumnIndex("toCityHighLight"));
                    toCityName = cursor.getString(cursor.getColumnIndex("toCityName"));

                    try {
                        dateFrom = dateFormatDataBase.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE)));
                        dateTo = dateFormatDataBase.parse(cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE)));
                    } catch (ParseException e) {
                        fromDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_DATE));
                        toDate = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_DATE));
                        e.printStackTrace();
                    }
                    fromDate = dateFormatPojo.format(dateFrom);
                    toDate = dateFormatPojo.format(dateTo);
                    fromTime = timeFormatPojo.format(dateFrom);
                    toTime = timeFormatPojo.format(dateTo);
                    fromInfo = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_FROM_INFO));
                    toInfo = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_TO_INFO));
                    info = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_INFO));
                    price = cursor.getInt(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_PRICE));
                    busId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_BUS_ID));
                    reservationCount = cursor.getInt(cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COLUMN_NAME_ROUTES_RESERVATION_COUNT));

                } while (cursor.moveToNext());
            }

            cursor.close();
            fromCity = new FromCityPojo(fromCityHighlight, fromCityId, fromCityName);
            toCity = new ToCityPojo(toCityHighlight, toCityId, toCityName);
            routeFullInfoPojo = new DatumPojo(routeId, fromCity, fromDate, fromTime, fromInfo,
                    toCity, toDate, toTime, toInfo, info, price, busId, reservationCount);

            subscriber.onNext(routeFullInfoPojo);
            subscriber.onCompleted();
        });
    }

    public void cleanSavedRoutes() {
        SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES, null, null);
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY, null, null);
        db.delete(DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY, null, null);
    }

    @Override
    protected void finalize() throws Throwable {
        getDatabaseHelper().close();
        super.finalize();
    }
}
