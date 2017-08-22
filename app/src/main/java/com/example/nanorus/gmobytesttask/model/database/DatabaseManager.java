package com.example.nanorus.gmobytesttask.model.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nanorus.gmobytesttask.model.DataMapper;
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
    private DatabaseContract mDatabaseContract;
    private DataMapper mDataMapper;


    @Inject
    DatabaseManager(DatabaseHelper databaseHelper, DatabaseContract databaseContract, DataMapper dataMapper) {
        mDatabaseHelper = databaseHelper;
        mDatabaseContract = databaseContract;
        mDataMapper = dataMapper;
        defineTablesColumnsNames();
    }


    private DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }


    // define names of tables and columns
    private String TABLE_NAME_ROUTES;
    private String TABLE_NAME_FROM_CITY;
    private String TABLE_NAME_TO_CITY;

    private String COLUMN_NAME_ROUTES_ID;
    private String COLUMN_NAME_ROUTES_FROM_DATE;
    private String COLUMN_NAME_ROUTES_TO_DATE;
    private String COLUMN_NAME_ROUTES_FROM_INFO;
    private String COLUMN_NAME_ROUTES_TO_INFO;
    private String COLUMN_NAME_ROUTES_FROM_CITY;
    private String COLUMN_NAME_ROUTES_TO_CITY;
    private String COLUMN_NAME_ROUTES_PRICE;
    private String COLUMN_NAME_ROUTES_INFO;
    private String COLUMN_NAME_ROUTES_BUS_ID;
    private String COLUMN_NAME_ROUTES_RESERVATION_COUNT;

    private String COLUMN_NAME_FROM_CITY_NAME;
    private String COLUMN_NAME_FROM_CITY_ID;
    private String COLUMN_NAME_TO_CITY_NAME;
    private String COLUMN_NAME_FROM_CITY_HIGHLIGHT;
    private String COLUMN_NAME_TO_CITY_ID;
    private String COLUMN_NAME_TO_CITY_HIGHLIGHT;

    private final String COMMA_SEP = ",";

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

                contentValues.put(mDatabaseContract.COLUMN_NAME_FROM_CITY_ID, fromCityPojo.getId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_FROM_CITY_HIGHLIGHT, fromCityPojo.getHighlight());
                contentValues.put(mDatabaseContract.COLUMN_NAME_FROM_CITY_NAME, fromCityPojo.getName());

                database.insertWithOnConflict(mDatabaseContract.TABLE_NAME_FROM_CITY,
                        null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                contentValues.clear();
            }
            fromCityPojo = null;

            // insert to To City table
            for (int i = 0; i < routesFullInfo.getData().size(); i++) {
                toCityPojo = routesFullInfo.getData().get(i).getToCity();

                contentValues.put(mDatabaseContract.COLUMN_NAME_TO_CITY_ID, toCityPojo.getId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_TO_CITY_HIGHLIGHT, toCityPojo.getHighlight());
                contentValues.put(mDatabaseContract.COLUMN_NAME_TO_CITY_NAME, toCityPojo.getName());


                database.insertWithOnConflict(mDatabaseContract.TABLE_NAME_TO_CITY,
                        null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                contentValues.clear();
            }
            toCityPojo = null;

            // insert to Routes table
            for (int i = 0; i < routesFullInfo.getData().size(); i++) {
                datumPojo = routesFullInfo.getData().get(i);

                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_ID, datumPojo.getId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_CITY, datumPojo.getFromCity().getId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_TO_CITY, datumPojo.getToCity().getId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_DATE,
                        datumPojo.getFromDate() + " " + datumPojo.getFromTime());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_TO_DATE,
                        datumPojo.getToDate() + " " + datumPojo.getToTime());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_PRICE, datumPojo.getPrice());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_INFO, datumPojo.getFromInfo());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_TO_INFO, datumPojo.getToInfo());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_INFO, datumPojo.getInfo());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_BUS_ID, datumPojo.getBusId());
                contentValues.put(mDatabaseContract.COLUMN_NAME_ROUTES_RESERVATION_COUNT, datumPojo.getReservationCount());

                database.insertWithOnConflict(mDatabaseContract.TABLE_NAME_ROUTES,
                        null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);

                contentValues.clear();
            }
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

                                    " FROM " + mDatabaseContract.TABLE_NAME_ROUTES +

                                    " INNER JOIN " + TABLE_NAME_FROM_CITY + " ON " +
                                    TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_FROM_CITY + " = " +
                                    TABLE_NAME_FROM_CITY + "." + mDatabaseContract.COLUMN_NAME_FROM_CITY_ID +

                                    " INNER JOIN " + TABLE_NAME_TO_CITY + " ON " +
                                    TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_TO_CITY + " = " +
                                    TABLE_NAME_TO_CITY + "." + mDatabaseContract.COLUMN_NAME_TO_CITY_ID
                            , null);
                    if (cursor.moveToFirst()) {
                        do {
                            RouteMainInfoPojo routeMainInfoPojo = new RouteMainInfoPojo(
                                    cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("FromCity")),
                                    cursor.getString(cursor.getColumnIndex("ToCity")),
                                    mDataMapper.apiDateFormatToCorrectDateFormat(cursor.getString(cursor.getColumnIndex("FromDate"))),
                                    mDataMapper.apiDateFormatToCorrectDateFormat(cursor.getString(cursor.getColumnIndex("ToDate"))),
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
                        dateFrom = dateFormatDataBase.parse(cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_DATE)));
                        dateTo = dateFormatDataBase.parse(cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_TO_DATE)));
                    } catch (ParseException e) {
                        fromDate = cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_DATE));
                        toDate = cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_TO_DATE));
                        e.printStackTrace();
                    }
                    fromDate = dateFormatPojo.format(dateFrom);
                    toDate = dateFormatPojo.format(dateTo);
                    fromTime = timeFormatPojo.format(dateFrom);
                    toTime = timeFormatPojo.format(dateTo);
                    fromInfo = cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_FROM_INFO));
                    toInfo = cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_TO_INFO));
                    info = cursor.getString(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_INFO));
                    price = cursor.getInt(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_PRICE));
                    busId = cursor.getInt(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_BUS_ID));
                    reservationCount = cursor.getInt(cursor.getColumnIndex(mDatabaseContract.COLUMN_NAME_ROUTES_RESERVATION_COUNT));

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
        db.delete(mDatabaseContract.TABLE_NAME_ROUTES, null, null);
        db.delete(mDatabaseContract.TABLE_NAME_FROM_CITY, null, null);
        db.delete(mDatabaseContract.TABLE_NAME_TO_CITY, null, null);
    }

    private void defineTablesColumnsNames() {
        TABLE_NAME_ROUTES = mDatabaseContract.TABLE_NAME_ROUTES;
        TABLE_NAME_FROM_CITY = mDatabaseContract.TABLE_NAME_FROM_CITY;
        TABLE_NAME_TO_CITY = mDatabaseContract.TABLE_NAME_TO_CITY;
        COLUMN_NAME_ROUTES_ID = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_ID;
        COLUMN_NAME_ROUTES_FROM_DATE = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_FROM_DATE;
        COLUMN_NAME_ROUTES_TO_DATE = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_TO_DATE;
        COLUMN_NAME_ROUTES_FROM_INFO = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_FROM_INFO;
        COLUMN_NAME_ROUTES_TO_INFO = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_TO_INFO;
        COLUMN_NAME_ROUTES_FROM_CITY = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_FROM_CITY;
        COLUMN_NAME_ROUTES_TO_CITY = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_TO_CITY;
        COLUMN_NAME_ROUTES_PRICE = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_PRICE;
        COLUMN_NAME_ROUTES_INFO = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_INFO;
        COLUMN_NAME_ROUTES_BUS_ID = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_BUS_ID;
        COLUMN_NAME_ROUTES_RESERVATION_COUNT = TABLE_NAME_ROUTES + "." + mDatabaseContract.COLUMN_NAME_ROUTES_RESERVATION_COUNT;
        COLUMN_NAME_FROM_CITY_NAME = TABLE_NAME_FROM_CITY + "." + mDatabaseContract.COLUMN_NAME_FROM_CITY_NAME;
        COLUMN_NAME_FROM_CITY_ID = TABLE_NAME_FROM_CITY + "." + mDatabaseContract.COLUMN_NAME_FROM_CITY_ID;
        COLUMN_NAME_TO_CITY_NAME = TABLE_NAME_TO_CITY + "." + mDatabaseContract.COLUMN_NAME_TO_CITY_NAME;
        COLUMN_NAME_FROM_CITY_HIGHLIGHT = TABLE_NAME_FROM_CITY + "." + mDatabaseContract.COLUMN_NAME_FROM_CITY_HIGHLIGHT;
        COLUMN_NAME_TO_CITY_ID = TABLE_NAME_TO_CITY + "." + mDatabaseContract.COLUMN_NAME_TO_CITY_ID;
        COLUMN_NAME_TO_CITY_HIGHLIGHT = TABLE_NAME_TO_CITY + "." + mDatabaseContract.COLUMN_NAME_TO_CITY_HIGHLIGHT;
    }
    @Override
    protected void finalize() throws Throwable {
        getDatabaseHelper().close();
        super.finalize();
    }
}
