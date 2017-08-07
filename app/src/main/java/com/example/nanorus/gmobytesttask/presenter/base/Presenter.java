package com.example.nanorus.gmobytesttask.presenter.base;

public interface Presenter<V> {
    void onViewAttached(V view);

    void onViewDetached();

    void onDestroyed();
}
