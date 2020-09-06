package com.diet.trinity.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diet.trinity.MainActivity;
import com.diet.trinity.R;
import com.diet.trinity.Utility.Common;
import com.diet.trinity.Utility.Global;
import com.diet.trinity.model.PersonalData;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.res.Configuration;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends AppCompatActivity {
    EditText tallEdit, weightEdit, birthEdit, neckEdit, waistEdit, thighEdit;
    RadioButton manBtn, womanBtn, ex0, ex3,ex5, ex7, exSports;
    RadioGroup settingR, genderR;
    IndicatorSeekBar seekBar;
    LinearLayout linSportContainer;
    int gymNum;
    Calendar calendar;
    Date mBirthday;
    int mWeeklyReduce = 300;
    RequestQueue queue;

    //------calendar---------//
    private CalendarView mCalendarView;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        String languageToLoad  = "Latn"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        setContentView(R.layout.activity_setting);
        init();
        addEventListener();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String bitdate = dateFormat.format(PersonalData.getInstance().getBirthday());

        tallEdit.setText( String.valueOf((PersonalData.getInstance().getHeight())));
        weightEdit.setText(String.valueOf(PersonalData.getInstance().getWeight()));
        birthEdit.setText(String.valueOf(bitdate));
        neckEdit.setText(String.valueOf(PersonalData.getInstance().getNeck_perimeter()));
        waistEdit.setText(String.valueOf(PersonalData.getInstance().getWaist_perimeter()));
        thighEdit.setText(String.valueOf(PersonalData.getInstance().getThigh_perimeter()));

        if(String.valueOf(PersonalData.getInstance().getGender()).equals("MALE")){
            manBtn.setChecked(true);
        }else {
            womanBtn.setChecked(true);
        }

        gymNum = PersonalData.getInstance().getGymType();
        switch (gymNum){
            case 0:
                ex0.setChecked(true);
                break;
            case 1:
                ex3.setChecked(true);
                break;
            case 2:
                ex5.setChecked(true);
                break;
            case 3:
                ex7.setChecked(true);
                break;
            case -1:
                exSports.setChecked(true);
                break;
            default:
                break;
        }

        seekBar.setProgress(PersonalData.getInstance().getWeekly_reduce());
        seekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                switch (seekParams.thumbPosition){
                    case 0:
                        mWeeklyReduce = 300;
                        break;
                    case 1:
                        mWeeklyReduce = 600;
                        break;
                    case 2:
                        mWeeklyReduce = 1000;
                        break;
                    case 3:
                        mWeeklyReduce = 1500;
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        genderR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("gender", String.valueOf(checkedId));
            }
        });

        mWeeklyReduce = (int) PersonalData.getInstance().getWeekly_reduce();
        if(!exSports.isChecked()){
            linSportContainer.setVisibility(View.GONE);
        }

        settingR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("checkedId", String.valueOf(checkedId));
                gymNum = checkedId % 5 - 1;
                Toast.makeText(getBaseContext(), String.valueOf(gymNum), Toast.LENGTH_SHORT).show();
                if(gymNum == -1){
                    gymNum = 4;
                    linSportContainer.setVisibility(View.VISIBLE);
                }
                else{
                    linSportContainer.setVisibility(View.GONE);
                }
            }
        });

        //--------calendar----------//
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

    private void addEventListener() {
        final TextView _count = findViewById(R.id.txtSelectedMealCount);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    calendar = Calendar.getInstance();
                    String currentString = birthEdit.getText().toString();
                    String[] separated = currentString.split("/");
                    calendar.set(Integer.parseInt(separated[2]), Integer.parseInt(separated[1]), Integer.parseInt(separated[0]));
                    mBirthday = calendar.getTime();


                    PersonalData.getInstance().setHeight(Float.parseFloat(tallEdit.getText().toString()));
                    PersonalData.getInstance().setWeight(Float.parseFloat(weightEdit.getText().toString()));
                    PersonalData.getInstance().setBirthday(mBirthday);
                    PersonalData.getInstance().setNeck_perimeter(Float.parseFloat(neckEdit.getText().toString()));
                    PersonalData.getInstance().setWaist_perimeter(Float.parseFloat(waistEdit.getText().toString()));
                    PersonalData.getInstance().setThigh_perimeter(Float.parseFloat(thighEdit.getText().toString()));
                    PersonalData.getInstance().setGymType(gymNum);
                    PersonalData.getInstance().setWeekly_reduce(mWeeklyReduce);

                    sendSettings();

                    Intent intent = new Intent(SettingActivity.this, DailyCaleandarActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    private void init() {
        tallEdit = findViewById(R.id.tallEdit);
        weightEdit = findViewById(R.id.weightEdit);
        birthEdit = findViewById(R.id.birthdayEdit);
        neckEdit = findViewById(R.id.neckEdit);
        waistEdit = findViewById(R.id.waistEdit);
        thighEdit = findViewById(R.id.thighEdit);
        manBtn = findViewById(R.id.genderMan);
        womanBtn = findViewById(R.id.genderWoman);
        settingR = findViewById(R.id.settingR);
        genderR = findViewById(R.id.genderradioG);
        ex0 = findViewById(R.id.ex0);
        ex3 = findViewById(R.id.ex3);
        ex5 = findViewById(R.id.ex5);
        ex7 = findViewById(R.id.ex7);
        exSports = findViewById(R.id.exSports);
        linSportContainer = findViewById(R.id.linSportContainer);
        seekBar = findViewById(R.id.seekWeeklyReduce);
    }

    private boolean validate(){
        boolean validate = true;
        if(neckEdit.getText().toString().equals("")){
            neckEdit.setError("Εισαγάγετε τα δεδομένα.");
            validate = false;
        }
        if(waistEdit.getText().toString().equals("")){
            waistEdit.setError("Εισαγάγετε τα δεδομένα.");
            validate = false;
        }
        if(thighEdit.getText().toString().equals("")){
            thighEdit.setError("Εισαγάγετε τα δεδομένα.");
            validate = false;
        }
        return validate;
    }

    private void sendSettings(){
        String settingUrl = Common.getInstance().getSettingUrl();
        final String access_token = Global.token;
        StringRequest postRequest = new StringRequest(Request.Method.GET, settingUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                             String result = jsonObject.getString("success");

                            if (result.equals("true")){

                            }
                            else {
                                Toast.makeText(SettingActivity.this, getResources().getString(R.string.retry_info_login), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(SettingActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                Log.d("getHeaders: ", access_token);
                headers.put("Authorization", "Bearer "+access_token);
                return headers;
            }

//            @Override
//            protected Map<String, String> getParams()
//            {
//                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//                String birthday = df.format(PersonalData.getInstance().getBirthday());
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("user_id", Global.user_id);
//                params.put("date", birthday);
//                params.put("exercise_rate", PersonalData.getInstance().getGymType()+"");
//                params.put("height", PersonalData.getInstance().getHeight()+"");
//                params.put("weight", PersonalData.getInstance().getWeight()+"");
//                params.put("neck", PersonalData.getInstance().getNeck_perimeter()+"");
//                params.put("waist", PersonalData.getInstance().getWaist_perimeter()+"");
//                params.put("thigh", PersonalData.getInstance().getThigh_perimeter()+"");
//                params.put("weekly_goal", PersonalData.getInstance().getWeekly_reduce()+"");
//                return params;
//            }
        };
        queue = Volley.newRequestQueue(SettingActivity.this);
        queue.add(postRequest);
    }
}