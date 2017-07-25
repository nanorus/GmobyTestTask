package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListSetIsOnlineLoadingEvent {

    private boolean isOnlineLoading;

    public RoutesListSetIsOnlineLoadingEvent(boolean isOnlineLoading) {
        this.isOnlineLoading = isOnlineLoading;
    }

    public boolean isOnlineLoading() {
        return isOnlineLoading;
    }

    public void setOnlineLoading(boolean OnlineLoading) {
        isOnlineLoading = OnlineLoading;
    }
}
