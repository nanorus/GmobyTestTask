package com.example.nanorus.gmobytesttask.test_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.nanorus.gmobytesttask.R;
import com.example.nanorus.gmobytesttask.view.routes_list.RoutesListActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChooseDateRoutesActivity extends AppCompatActivity implements View.OnClickListener {

    String mFromDate;
    String mToDate;
    Calendar mFromCalendar;
    Calendar mToCalendar;

    RelativeLayout mRootLayout;
    EditText mEditTextFromDate;
    EditText mEditTextToDate;
    Button mButtonGo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_routes);

        mEditTextFromDate = (EditText) findViewById(R.id.activity_choose_date_routes_tv_from_date);
        mEditTextToDate = (EditText) findViewById(R.id.activity_choose_date_routes_tv_to_date);
        mButtonGo = (Button) findViewById(R.id.activity_choose_date_routes_btn_go);
        mRootLayout = (RelativeLayout) findViewById(R.id.activity_choose_date_routes_rl_root);

        mEditTextFromDate.setOnClickListener(this);
        mEditTextToDate.setOnClickListener(this);
        mButtonGo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_choose_date_routes_tv_from_date:
                AlertDialog.Builder fromBuilder = new AlertDialog.Builder(this);
                DatePicker fromDatePicker = new DatePicker(this);
                fromBuilder.setView(fromDatePicker);
                fromBuilder.setPositiveButton("SET DATE", (dialogInterface, i) -> {
                    mFromCalendar = Calendar.getInstance();
                    System.out.println("Picker from month: " + fromDatePicker.getMonth());
                    mFromCalendar.set(fromDatePicker.getYear(), fromDatePicker.getMonth(), fromDatePicker.getDayOfMonth());

                    SimpleDateFormat materialFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

                    mEditTextFromDate.setText(materialFormat.format(mFromCalendar.getTime()));
                    String month = String.valueOf(fromDatePicker.getMonth() + 1);
                    if (month.length() == 1) {
                        month = 0 + month;
                    }
                    String day = String.valueOf(fromDatePicker.getDayOfMonth());
                    if (day.length() == 1) {
                        day = 0 + day;
                    }
                    mFromDate = String.valueOf(fromDatePicker.getYear()) + month + day;
                    System.out.println("Picker from date: " + mFromDate);

                });
                fromBuilder.setNegativeButton("CANCEL", ((dialogInterface, i) -> dialogInterface.dismiss()));
                fromBuilder.show();
                break;
            case R.id.activity_choose_date_routes_tv_to_date:
                AlertDialog.Builder toBuilder = new AlertDialog.Builder(this);
                DatePicker toDatePicker = new DatePicker(this);
                toBuilder.setView(toDatePicker);
                toBuilder.setPositiveButton("SET DATE", (dialogInterface, i) -> {
                    mToCalendar = Calendar.getInstance();
                    System.out.println("Picker to month: " + toDatePicker.getMonth());
                    mToCalendar.set(toDatePicker.getYear(), toDatePicker.getMonth(), toDatePicker.getDayOfMonth());

                    SimpleDateFormat materialFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

                    mEditTextToDate.setText(materialFormat.format(mToCalendar.getTime()));
                    String month = String.valueOf(toDatePicker.getMonth() + 1);
                    if (month.length() == 1) {
                        month = 0 + month;
                    }
                    String day = String.valueOf(toDatePicker.getDayOfMonth());
                    if (day.length() == 1) {
                        day = 0 + day;
                    }
                    mToDate = String.valueOf(toDatePicker.getYear()) + month + day;

                    System.out.println("Picker to date: " + mToDate);
                });
                toBuilder.setNegativeButton("CANCEL", ((dialogInterface, i) -> dialogInterface.dismiss()));
                toBuilder.show();
                break;
            case R.id.activity_choose_date_routes_btn_go:
                if (mFromDate == null || mToDate == null) {
                    showSnackBar("Выберите даты");
                } else if (mFromCalendar.getTime().getTime() > mToCalendar.getTime().getTime()) {
                    showSnackBar("Первая дата должна быть меньше или равна второй");
                } else {
                    Intent intent = new Intent(this, RoutesListActivity.class);
                    intent.putExtra("from_date", mFromDate);
                    intent.putExtra("to_date", mToDate);
                    startActivity(intent);
                }
                break;
        }
    }

    private void showSnackBar(String text) {
        Snackbar.make(mRootLayout, text, Snackbar.LENGTH_SHORT).show();
    }

}
