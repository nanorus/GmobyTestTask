package com.example.nanorus.gmobytesttask.app.bus.event.routes_list;

public class RoutesListShowRefreshingEvent {

   private boolean isShowRefresh = false;

    public RoutesListShowRefreshingEvent(boolean isShowRefresh) {
        this.isShowRefresh = isShowRefresh;
    }

    public boolean isShowRefresh() {
        return isShowRefresh;
    }

    public void setShowRefresh(boolean showRefresh) {
        isShowRefresh = showRefresh;
    }
}
