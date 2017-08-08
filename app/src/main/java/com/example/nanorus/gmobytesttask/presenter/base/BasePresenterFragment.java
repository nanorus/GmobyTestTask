package com.example.nanorus.gmobytesttask.presenter.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;

public abstract class BasePresenterFragment<P extends Presenter<V>, V> extends Fragment {

    private static final String TAG = "base-fragment";
    private static final int LOADER_ID = 101;

    private P presenter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Loader<P> loader = getLoaderManager().getLoader(loaderId());
        if (loader == null) {
            initLoader();
        } else {
            this.presenter = ((PresenterLoader<P>) loader).getPresenter();
            onPresenterCreatedOrRestored(presenter);
        }
    }

    private void initLoader() {
        // LoaderCallbacks as an object, so no hint regarding loader will be leak to the subclasses.
        getLoaderManager().initLoader(loaderId(), null, new LoaderManager.LoaderCallbacks<P>() {
            @Override
            public final Loader<P> onCreateLoader(int id, Bundle args) {
                return new PresenterLoader<>(getContext(), getPresenterFactory(), tag());
            }

            @Override
            public final void onLoadFinished(Loader<P> loader, P presenter) {
                BasePresenterFragment.this.presenter = presenter;
                onPresenterCreatedOrRestored(presenter);
            }

            @Override
            public final void onLoaderReset(Loader<P> loader) {
                BasePresenterFragment.this.presenter = null;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onViewAttached(getPresenterView());
    }

    @Override
    public void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    /**
     * String tag use for log purposes.
     */
    @NonNull
    protected abstract String tag();

    /**
     * Instance of {@link PresenterFactory} use to create a Presenter when needed. This instance should
     * not contain {@link android.app.Activity} context reference since it will be keep on rotations.
     */
    @NonNull
    protected abstract PresenterFactory<P> getPresenterFactory();

    /**
     * Hook for subclasses that deliver the {@link Presenter} before its View is attached.
     * Can be use to initialize the Presenter or simple hold a reference to it.
     */
    protected abstract void onPresenterCreatedOrRestored(@NonNull P presenter);

    /**
     * Override in case of fragment not implementing Presenter<View> interface
     */
    @NonNull
    protected V getPresenterView() {
        return (V) this;
    }

    /**
     * Use this method in case you want to specify a spefic ID for the {@link PresenterLoader}.
     * By default its value would be {@link #LOADER_ID}.
     */
    protected int loaderId() {
        return LOADER_ID;
    }
}
