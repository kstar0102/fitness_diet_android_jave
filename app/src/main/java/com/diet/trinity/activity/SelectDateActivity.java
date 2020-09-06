package com.diet.trinity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.diet.trinity.R;

public class SelectDateActivity extends AppCompatActivity {
    private  static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        alertDialogBuilder = new AlertDialog.Builder(this);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        mCalendarView.setFocusedMonthDateColor(Color.MAGENTA);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                String date = year + "/" + (month+1) + "/"+ dayOfMonth ;
                alertDialogBuilder.setMessage(date);
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}