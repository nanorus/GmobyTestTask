package com.example.nanorus.gmobytesttask.app.bus.event;

public class ShowRefreshingEvent {

   private boolean isShowRefresh = false;

    public ShowRefreshingEvent(boolean isShowRefresh) {
        this.isShowRefresh = isShowRefresh;
    }

    public boolean isShowRefresh() {
        return isShowRefresh;
    }

    public void setShowRefresh(boolean showRefresh) {
        isShowRefresh = showRefresh;
    }
}
