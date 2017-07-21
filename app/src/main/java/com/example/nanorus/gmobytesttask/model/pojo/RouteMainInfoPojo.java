package com.example.nanorus.gmobytesttask.model.pojo;

public class RouteMainInfoPojo {

    private int id;
    private String fromCityName;
    private String toCityName;
    private String fromDate;
    private String toDate;
    private int price;

    public RouteMainInfoPojo(int id, String fromCityName, String toCityName, String fromDate, String toDate, int price) {
        this.id = id;
        this.fromCityName = fromCityName;
        this.toCityName = toCityName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFromCityName() {
        return fromCityName;
    }

    public void setFromCityName(String fromCityName) {
        this.fromCityName = fromCityName;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
