package com.example.nanorus.gmobytesttask.app.bus.event;

public class RoutesListShowAlertEvent {

    private String message;
    private boolean willShown;

    public RoutesListShowAlertEvent(String message, boolean willShown) {
        this.message = message;
        this.willShown = willShown;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isWillShown() {
        return willShown;
    }

    public void setWillShown(boolean willShown) {
        this.willShown = willShown;
    }
}
