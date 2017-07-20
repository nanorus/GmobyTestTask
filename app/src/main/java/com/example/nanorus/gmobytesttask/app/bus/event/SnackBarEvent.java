package com.example.nanorus.gmobytesttask.app.bus.event;

public class SnackBarEvent {

    String mMessage;

    public SnackBarEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
