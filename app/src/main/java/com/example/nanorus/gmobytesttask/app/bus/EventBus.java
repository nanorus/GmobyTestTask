package com.example.nanorus.gmobytesttask.app.bus;

import com.squareup.otto.Bus;

public class EventBus {

    private static Bus sInstance = null;

    public static Bus getInstance() {
        if (sInstance == null)
            sInstance = new Bus();
        return sInstance;
    }

    @Override
    protected void finalize() throws Throwable {
        sInstance = null;
        super.finalize();
    }
}
