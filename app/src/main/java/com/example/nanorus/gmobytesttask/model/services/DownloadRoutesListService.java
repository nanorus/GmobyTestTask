package com.example.nanorus.gmobytesttask.model.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.nanorus.gmobytesttask.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadRoutesListService extends Service {

    public static String BROADCAST_ACTION = "com.example.nanorus.gmobytesttask.action.ROUTES_DOWNLOADED";
    public static String mResponse = null;

    Context mContext;

    public DownloadRoutesListService() {
        mContext = App.getApp().getApplicationContext();
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];

                URL obj;
                HttpURLConnection con;
                BufferedReader in;
                String inputLine;
                StringBuilder response = new StringBuilder();
                try {
                    obj = new URL(url);
                    con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                    in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return response.toString();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                setResponse(s);

                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra("is_data_downloaded", true);
                mContext.sendBroadcast(intent);

            }
        }
                .execute("http://projects.gmoby.org/web/index.php/api/trips?from_date=20140101&to_date=20180501");


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static String getResponse() {
        return mResponse;
    }

    public static void setResponse(String mResponse) {
        DownloadRoutesListService.mResponse = mResponse;
    }
}
