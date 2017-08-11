package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListSnackBarEvent {

    private String mMessage;

    public RoutesListSnackBarEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
