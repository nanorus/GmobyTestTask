package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataConverter {

    public static String convertApiDateFormatToCorrectDateFormat(String apiDateString){
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = parser.parse(apiDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH);
        return formatter.format(date);

    }

    public static Date convertStringToDate(String string) {
        Date date = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            date = format.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date convertStringToDate(String dateString, String timeString) {
        Date date = null;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            date = format.parse(dateString + " " + timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static List<RouteMainInfoPojo> convertFullRoutesListToMainInfoRouteList(List<DatumPojo> datumPojos) {
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();

        for (int i = 0; i < datumPojos.size(); i++) {
            DatumPojo datumPojo = datumPojos.get(i);
            routeMainInfoPojos.add(new RouteMainInfoPojo(
                    datumPojo.getId(),
                    datumPojo.getFromCity().getName(),
                    datumPojo.getToCity().getName(),
                    DataConverter.convertApiDateFormatToCorrectDateFormat(datumPojo.getFromDate() + " " + datumPojo.getFromTime()),
                    DataConverter.convertApiDateFormatToCorrectDateFormat(datumPojo.getToDate() + " " + datumPojo.getToTime()),
                    datumPojo.getPrice()
            ));
        }
        return routeMainInfoPojos;
    }

}
