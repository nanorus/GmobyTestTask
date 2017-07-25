package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListSnackBarEvent {

    String mMessage;

    public RoutesListSnackBarEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
