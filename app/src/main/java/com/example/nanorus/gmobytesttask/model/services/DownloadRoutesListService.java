package com.example.nanorus.gmobytesttask.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.nanorus.gmobytesttask.App;
import com.example.nanorus.gmobytesttask.model.DataManager;
import com.example.nanorus.gmobytesttask.model.DataMapper;
import com.example.nanorus.gmobytesttask.model.pojo.api.RequestPojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.inject.Inject;


public class DownloadRoutesListService extends Service {

    public static String BROADCAST_ACTION = "com.example.nanorus.gmobytesttask.action.ROUTES_DOWNLOADED";

    @Inject
    Context mContext;
    @Inject
    DataMapper mDataMapper;
    @Inject
    DataManager mDataManager;

    AsyncTask<String, Void, Boolean> mRequestAsyncTask;


    public DownloadRoutesListService() {
        App.getApp().getAppComponent().inject(this);
        App.getApp().getDataManagerComponent().inject(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mRequestAsyncTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... strings) {
                String url = strings[0];

                URL obj;
                HttpURLConnection con;
                BufferedReader in;
                String inputLine;
                StringBuilder response = new StringBuilder();
                if (!isCancelled()) {
                    try {
                        obj = new URL(url);
                        con = (HttpURLConnection) obj.openConnection();
                        con.setRequestMethod("GET");
                        in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        in.close();

                        // save response to db
                        RequestPojo requestPojo = mDataMapper.jsonToRequestPojo(response.toString());
                        mDataManager.cleanSavedRoutes();
                        mDataManager.saveRoutes(requestPojo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mDataManager.cleanSavedRoutes();
                    }
                    return true;
                } else
                    return false;
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                super.onPostExecute(isSuccessful);
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra("is_data_downloaded", true);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                DownloadRoutesListService.this.stopSelf();
            }
        }
                .execute("http://projects.gmoby.org/web/index.php/api/trips?from_date=20140101&to_date=20180501");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRequestAsyncTask.cancel(false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
