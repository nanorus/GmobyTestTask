package com.example.nanorus.gmobytesttask.model;

import com.example.nanorus.gmobytesttask.model.pojo.RouteMainInfoPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.DatumPojo;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class DataMapper {

    Gson mGson;

    public DataMapper(Gson gson) {
        mGson = gson;
    }

    public String apiDateFormatToCorrectDateFormat(String apiDateString) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = parser.parse(apiDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.ENGLISH);
        return formatter.format(date);

    }

    public List<RouteMainInfoPojo> fullRoutesListToMainInfoRouteList(List<DatumPojo> datumPojos) {
        ArrayList<RouteMainInfoPojo> routeMainInfoPojos = new ArrayList<>();

        for (int i = 0; i < datumPojos.size(); i++) {
            DatumPojo datumPojo = datumPojos.get(i);
            routeMainInfoPojos.add(new RouteMainInfoPojo(
                    datumPojo.getId(),
                    datumPojo.getFromCity().getName(),
                    datumPojo.getToCity().getName(),
                    apiDateFormatToCorrectDateFormat(datumPojo.getFromDate() + " " + datumPojo.getFromTime()),
                    apiDateFormatToCorrectDateFormat(datumPojo.getToDate() + " " + datumPojo.getToTime()),
                    datumPojo.getPrice()
            ));
        }
        return routeMainInfoPojos;
    }

    public RequestPojo jsonToRequestPojo(String json) {
        return mGson.fromJson(json, RequestPojo.class);
    }


}
