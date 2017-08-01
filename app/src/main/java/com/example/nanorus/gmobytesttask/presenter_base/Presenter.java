package com.example.nanorus.gmobytesttask.presenter_base;

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onDestroyed();
}
