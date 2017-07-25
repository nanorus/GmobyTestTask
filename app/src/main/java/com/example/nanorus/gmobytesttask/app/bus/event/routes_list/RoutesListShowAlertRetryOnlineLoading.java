package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListShowAlertRetryOnlineLoading {

    private String mMessage;

    public RoutesListShowAlertRetryOnlineLoading(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
