package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListShowNoDataEvent {

    boolean willShown;

    public RoutesListShowNoDataEvent(boolean willShown) {
        this.willShown = willShown;
    }

    public boolean isWillShown() {
        return willShown;
    }

    public void setWillShown(boolean willShown) {
        this.willShown = willShown;
    }
}
