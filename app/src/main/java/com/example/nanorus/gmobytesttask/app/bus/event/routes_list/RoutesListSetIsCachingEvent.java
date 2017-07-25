package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListSetIsCachingEvent {

    private boolean isCaching;

    public RoutesListSetIsCachingEvent(boolean isCaching) {
        this.isCaching = isCaching;
    }

    public boolean isCaching() {
        return isCaching;
    }

    public void setCaching(boolean caching) {
        isCaching = caching;
    }
}
