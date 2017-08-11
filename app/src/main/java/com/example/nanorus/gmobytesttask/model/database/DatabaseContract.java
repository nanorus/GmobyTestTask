package com.example.nanorus.gmobytesttask.model.database;

import android.provider.BaseColumns;

import static com.example.nanorus.gmobytesttask.model.database.DatabaseContract.DatabaseEntry.TABLE_NAME_FROM_CITY;
import static com.example.nanorus.gmobytesttask.model.database.DatabaseContract.DatabaseEntry.TABLE_NAME_ROUTES;
import static com.example.nanorus.gmobytesttask.model.database.DatabaseContract.DatabaseEntry.TABLE_NAME_TO_CITY;

class DatabaseContract {

    public DatabaseContract() {
    }

    public static abstract class DatabaseEntry implements BaseColumns {

        public static final String COMMA_SEP = ",";

        public static final String TABLE_NAME_ROUTES = "routes";
        public static final String TABLE_NAME_FROM_CITY = "from_city";
        public static final String TABLE_NAME_TO_CITY = "to_city";

        public static final String COLUMN_NAME_ROUTES_ID = "_id";
        public static final String COLUMN_NAME_ROUTES_FROM_CITY = "from_city";
        public static final String COLUMN_NAME_ROUTES_TO_CITY = "to_city";
        public static final String COLUMN_NAME_ROUTES_FROM_DATE = "from_date";
        public static final String COLUMN_NAME_ROUTES_TO_DATE = "to_date";
        public static final String COLUMN_NAME_ROUTES_PRICE = "price";
        public static final String COLUMN_NAME_ROUTES_FROM_INFO = "from_info";
        public static final String COLUMN_NAME_ROUTES_TO_INFO = "to_info";
        public static final String COLUMN_NAME_ROUTES_INFO = "info";
        public static final String COLUMN_NAME_ROUTES_BUS_ID = "bus_id";
        public static final String COLUMN_NAME_ROUTES_RESERVATION_COUNT = "reservation_count";

        public static final String COLUMN_NAME_FROM_CITY_ID = "_id";
        public static final String COLUMN_NAME_FROM_CITY_HIGHLIGHT = "highlight";
        public static final String COLUMN_NAME_FROM_CITY_NAME = "name";

        public static final String COLUMN_NAME_TO_CITY_ID = "_id";
        public static final String COLUMN_NAME_TO_CITY_HIGHLIGHT = "highlight";
        public static final String COLUMN_NAME_TO_CITY_NAME = "name";

        public static final String SQL_CREATE_TABLE_FROM_CITY =
                "CREATE TABLE " + TABLE_NAME_FROM_CITY + " (" +
                        COLUMN_NAME_FROM_CITY_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_FROM_CITY_NAME + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_FROM_CITY_HIGHLIGHT + " INTEGER" +
                        ")";
        public static final String SQL_CREATE_TABLE_TO_CITY =
                "CREATE TABLE " + TABLE_NAME_TO_CITY + " (" +
                        COLUMN_NAME_TO_CITY_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_TO_CITY_NAME + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_TO_CITY_HIGHLIGHT + " INTEGER"
                        + ")";

        public static final String SQL_CREATE_TABLE_ROUTES =
                "CREATE TABLE " + TABLE_NAME_ROUTES + "(" +
                        COLUMN_NAME_ROUTES_ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_FROM_CITY + " INTEGER" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_TO_CITY + " INTEGER" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_FROM_DATE + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_TO_DATE + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_FROM_INFO + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_TO_INFO + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_INFO + " TEXT" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_PRICE + " INTEGER" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_BUS_ID + " INTEGER" + COMMA_SEP +
                        COLUMN_NAME_ROUTES_RESERVATION_COUNT + " INTEGER" + COMMA_SEP +

                        "FOREIGN KEY(" + COLUMN_NAME_ROUTES_FROM_CITY + ") REFERENCES " +
                        TABLE_NAME_FROM_CITY + "(" + COLUMN_NAME_FROM_CITY_ID + ")" +
                        "FOREIGN KEY(" + COLUMN_NAME_ROUTES_TO_CITY + ") REFERENCES " +
                        TABLE_NAME_TO_CITY + "(" + COLUMN_NAME_TO_CITY_ID + ")" +
                        ")";
    }

    private static final String SQL_DELETE_TABLE_ROUTES = "DROP TABLE IF EXISTS " + TABLE_NAME_ROUTES;
    private static final String SQL_DELETE_TABLE_FROM_CITY = "DROP TABLE IF EXISTS " + TABLE_NAME_FROM_CITY;
    private static final String SQL_DELETE_TABLE_TO_CITY = "DROP TABLE IF EXISTS " + TABLE_NAME_TO_CITY;

}
