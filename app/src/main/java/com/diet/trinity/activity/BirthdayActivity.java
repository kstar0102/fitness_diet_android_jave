package com.diet.trinity.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diet.trinity.R;
import com.diet.trinity.model.PersonalData;

import org.joda.time.DateTime;
import org.joda.time.Years;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BirthdayActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Context mContext;
    TextView _brithday;
    Calendar calendar;
    Date mBirthday;
    int mAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);

        mContext = this;
        _brithday = findViewById(R.id.txtBirthday);
        calendar = Calendar.getInstance();
        calendar.set(1999, 4, 25);
        mBirthday = calendar.getTime();

        addEventListener();
    }

    private void addEventListener(){
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    PersonalData.getInstance().setBirthday(mBirthday);
                    PersonalData.getInstance().setAge(mAge);
                    PersonalData.getInstance().setStart_date(new Date());

                    Intent intent = new Intent(getBaseContext(), SportActivity.class);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.txtBirthday).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(), mBirthday.toString(), Toast.LENGTH_LONG).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        mContext, AlertDialog.THEME_HOLO_LIGHT, BirthdayActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private boolean validate(){
        boolean validate = true;
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
//        _brithday = findViewById(R.id.txtBirthday);
//
//        try{
//            mBirthday = format.parse(_brithday.getText().toString());
//        }
//        catch (Exception e){
//            mBirthday = new Date(1800,1,1);
//        }

        DateTime end = new DateTime(new Date());
        DateTime start = new DateTime(mBirthday);

        Years years = Years.yearsBetween(start, end);
        mAge = years.getYears();

        //Toast.makeText(getBaseContext(), String.valueOf(mAge), Toast.LENGTH_LONG).show();

        if((mAge < 0) || (mAge > 150)){
            _brithday.setError("Ημερομηνία Γέννησης");
            validate = false;
        }
        else{
            _brithday.setError(null);
        }
        return validate;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        calendar.set(year, month, dayOfMonth);
        mBirthday = calendar.getTime();
        _brithday.setText( formatter.format(mBirthday));
    }
}