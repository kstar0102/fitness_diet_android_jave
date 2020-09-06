package com.diet.trinity.activity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.diet.trinity.MyNotificationPublisher;
import com.diet.trinity.R;
import com.diet.trinity.Utility.FoodDatabaseHelper;
import com.diet.trinity.Utility.Global;
import com.diet.trinity.Utility.MealDatabaseHelper;
import com.diet.trinity.Utility.PersonalDatabaseHelper;
import com.diet.trinity.model.DietMode;
import com.diet.trinity.model.Goal;
import com.diet.trinity.model.Listmodel;
import com.diet.trinity.model.PersonalData;

import org.joda.time.DateTime;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimePrinter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//--------chart------------//
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;

import com.diet.trinity.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DailyCaleandarActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    TextView _date, _week, _currentWeight;
    LinearLayout _point, _unit, meal_add;
    ImageView _pointBalerina, _unitBalerina, water1, water2, water3, water4, water5, water6, water7, water8, fruit1, fruit2, fruit3;
    Date mSelectedDate;
    Calendar mCalendar;
    ScrollView screen;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    float y=0;

    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_caleandar);

        mCalendar = Calendar.getInstance();

        addEventListener();
        initView();
        initMeal();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String time = localDateFormat.format(new Date());
        Log.e("today hour", time);
        if(time.equals("09:00")){
            scheduleNotification(getNotification( "ΠΡΩΙΝΟ ΓΕΥΜΑ","το πρωινό είναι το πιο σημαντικό γεύμα της ημέρας" ) , 0 );
        }
        if(time.equals("12:00")){
            scheduleNotification(getNotification( "μεσημεριανό","για μια πιο υγιεινή ζωή μειώστε το αλάτι και τη ζάχαρη στα γεύματά σας" ) , 0 );
        }
        if(time.equals("13:00")){
            scheduleNotification(getNotification( "χρόνος νερού","νερό παρακαλώ" ) , 0 );
        }
        if(time.equals("20:00")){
            scheduleNotification(getNotification( "δείπνο","2 ώρες πριν τον ύπνο, δεν χρειάζεται πλέον φαγητό" ) , 0 );
        }

        meal_add = findViewById(R.id.meal_add);
        float yy=meal_add.getY();
        Integer yPos= getIntent().getIntExtra("yPos",0);
        Log.d( "onCreate: ", yy+"");
        screen = findViewById(R.id.screen);
    }


    private void initView(){
        //TextView alert = findViewById(R.id.alert);
//        alert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                scheduleNotification(getNotification( "ΠΡΩΙΝΟ ΓΕΥΜΑ","το πρωινό είναι το πιο σημαντικό γεύμα της ημέρας" ) , 0 );
//
//            }
//        });
        _date = findViewById(R.id.txtDate);
        _week = findViewById(R.id.txtWeek);

        TextView _pointValue = findViewById(R.id.txtPoint);
        TextView _unitValue = findViewById(R.id.txtUnit);
        _unitValue.setText(String.valueOf(PersonalData.getInstance().getUnits()));
        _pointValue.setText(String.valueOf(PersonalData.getInstance().getPoints()));

        TextView _BFP = findViewById(R.id.txtBFP);
        TextView _IDW = findViewById(R.id.txtIDW);
        TextView _BMI = findViewById(R.id.txtBMI);
        TextView _BMIText = findViewById(R.id.txtBMIText);
        float bfp = PersonalData.getInstance().getBFP();
        float idw = PersonalData.getInstance().getIdeal_weight();
        float bmi = PersonalData.getInstance().getBMI().getValue();
        String bmiState = PersonalData.getInstance().getBMI().getState();
        if(bfp < 0){
            _BFP.setText("0%");
            _BFP.setTextSize(18);
        }else {
            _BFP.setText(String.format(Locale.US,"%.1f", bfp) + " %");
        }
        _IDW.setText(String.format(Locale.US, "%.1f Kg", idw));
        _BMI.setText(String.format(Locale.US, "%.1f", bmi));
        _BMIText.setText(bmiState);

        TextView _timerBreakfast = findViewById(R.id.txtTimerBreakFast);
        TextView _timerLunch = findViewById(R.id.txtTimerLunch);
        TextView _timerDinner = findViewById(R.id.txtTimerDinner);
        TextView _timerSnackBreakfast = findViewById(R.id.txtTimerSnackBreakfast);
        TextView _timerSnackLunch = findViewById(R.id.txtTimerSnackLunch);
        TextView _timerSnackDinner = findViewById(R.id.txtTimerSnackDinner);
        final TextView appleT = findViewById(R.id.apple_txt);
        final TextView water_L = findViewById(R.id.water_L);
        Goal goal = PersonalData.getInstance().getGoal();
        _timerBreakfast.setText(goal == Goal.LOSE ? "15:00": "30:00");
        _timerLunch.setText(goal == Goal.LOSE ? "10:00": "20:00");
        _timerDinner.setText(goal == Goal.LOSE ? "10:00": "20:00");
        _timerSnackBreakfast.setText(goal == Goal.LOSE ? "05:00": "13:00");
        _timerSnackLunch.setText(goal == Goal.LOSE ? "05:00": "13:00");
        _timerSnackDinner.setText(goal == Goal.LOSE ? "05:00": "13:00");

        _currentWeight = findViewById(R.id.txtCurrentWeight);
        _currentWeight.setText(String.format(Locale.US, "%.1f", PersonalData.getInstance().getWeight()));

        TextView _initial = findViewById(R.id.txtInitialWeight);
        TextView _ideal = findViewById(R.id.txtIdealWeight);
        TextView _today = findViewById(R.id.txtTodayWeight);
        TextView _diff = findViewById(R.id.txtWeightDiff);
        _initial.setText(String.format(Locale.US, "%.1f Kg", PersonalData.getInstance().getInitial_weight()));
        _ideal.setText(String.format(Locale.US, "%.1f Kg", PersonalData.getInstance().getIdeal_weight()));
        _today.setText(String.format(Locale.US, "%.1f Kg", PersonalData.getInstance().getWeight()));
        _diff.setText(String.format(Locale.US, "%.1f Kg", PersonalData.getInstance().getWeight() - PersonalData.getInstance().getInitial_weight()));

        if(PersonalData.getInstance().getDietMode() == DietMode.POINT){
            _point.performClick();
        }
        else{
            _unit.performClick();
        }

        fruit1 = findViewById(R.id.fruit1);
        fruit2 = findViewById(R.id.fruit2);
        fruit3 = findViewById(R.id.fruit3);

        fruit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appleT.setText("1 φ.");
                fruit2.setImageResource(R.drawable.ic_apple_plus);
                fruit3.setImageResource(R.drawable.ic_apple_idle);
                fruit1.setImageResource(R.drawable.ic_apple_eaten);

            }
        });

        fruit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appleT.setText("2 φ.");
                fruit2.setImageResource(R.drawable.ic_apple_eaten);
                fruit3.setImageResource(R.drawable.ic_apple_plus);
                fruit1.setImageResource(R.drawable.ic_apple_eaten);
            }
        });

        fruit3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appleT.setText("3 φ.");
                fruit3.setImageResource(R.drawable.ic_apple_eaten);
                fruit2.setImageResource(R.drawable.ic_apple_eaten);
                fruit1.setImageResource(R.drawable.ic_apple_eaten);
            }
        });

        water1 = findViewById(R.id.water1);
        water2 = findViewById(R.id.water2);
        water3 = findViewById(R.id.water3);
        water4 = findViewById(R.id.water4);
        water5 = findViewById(R.id.water5);
        water6 = findViewById(R.id.water6);
        water7 = findViewById(R.id.water7);
        water8 = findViewById(R.id.water8);

        water1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water3.setImageResource(R.drawable.ic_water_cup_empty);
                water4.setImageResource(R.drawable.ic_water_cup_empty);
                water5.setImageResource(R.drawable.ic_water_cup_empty);
                water6.setImageResource(R.drawable.ic_water_cup_empty);
                water7.setImageResource(R.drawable.ic_water_cup_empty);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
            }
        });

        water2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water4.setImageResource(R.drawable.ic_water_cup_empty);
                water5.setImageResource(R.drawable.ic_water_cup_empty);
                water6.setImageResource(R.drawable.ic_water_cup_empty);
                water7.setImageResource(R.drawable.ic_water_cup_empty);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
                water_L.setText("0.5 L");
            }
        });

        water3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water5.setImageResource(R.drawable.ic_water_cup_empty);
                water6.setImageResource(R.drawable.ic_water_cup_empty);
                water7.setImageResource(R.drawable.ic_water_cup_empty);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
                water_L.setText("0.75 L");
            }
        });

        water4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_full);
                water5.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water6.setImageResource(R.drawable.ic_water_cup_empty);
                water7.setImageResource(R.drawable.ic_water_cup_empty);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
                water_L.setText("1.0 L");
            }
        });

        water5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_full);
                water5.setImageResource(R.drawable.ic_water_cup_full);
                water6.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water7.setImageResource(R.drawable.ic_water_cup_empty);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
                water_L.setText("1.25 L");
            }
        });

        water6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_full);
                water5.setImageResource(R.drawable.ic_water_cup_full);
                water6.setImageResource(R.drawable.ic_water_cup_full);
                water7.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water8.setImageResource(R.drawable.ic_water_cup_empty);
                water_L.setText("1.5 L");
            }
        });

        water7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_full);
                water5.setImageResource(R.drawable.ic_water_cup_full);
                water6.setImageResource(R.drawable.ic_water_cup_full);
                water7.setImageResource(R.drawable.ic_water_cup_full);
                water8.setImageResource(R.drawable.ic_water_cup_empty_plus);
                water_L.setText("1.75 L");
            }
        });

        water8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                water1.setImageResource(R.drawable.ic_water_cup_full);
                water2.setImageResource(R.drawable.ic_water_cup_full);
                water3.setImageResource(R.drawable.ic_water_cup_full);
                water4.setImageResource(R.drawable.ic_water_cup_full);
                water5.setImageResource(R.drawable.ic_water_cup_full);
                water6.setImageResource(R.drawable.ic_water_cup_full);
                water7.setImageResource(R.drawable.ic_water_cup_full);
                water8.setImageResource(R.drawable.ic_water_cup_full);
                water_L.setText("2.0 L");
            }
        });

        //--------chart------------//

        // we get graph view instance
        GraphView graph = (GraphView) findViewById(R.id.graph);
        // data
        series = new LineGraphSeries<DataPoint>();
        series.appendData(new DataPoint(0,PersonalData.getInstance().getIdeal_weight()), true, 100);
        graphSeries();

        series.setColor(Color.CYAN);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        graph.addSeries(series);

        // styling grid/labels

        graph.getGridLabelRenderer().reloadStyles();
        graph.getGridLabelRenderer().setHorizontalAxisTitleColor(Color.GREEN);

        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);

        viewport.setXAxisBoundsManual(true);
        viewport.scrollToEnd();
        viewport.setMinY(0);
        viewport.setMaxY(10);

        viewport.setScrollable(true);
        viewport.setScalable(true);
        viewport.setScalableY(true);
        viewport.setScrollableY(true);
    }

    private void scheduleNotification (Notification notification , int delay) {
        Intent notificationIntent = new Intent( this, MyNotificationPublisher. class ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION_ID , 1 ) ;
        notificationIntent.putExtra(MyNotificationPublisher. NOTIFICATION , notification) ;
        PendingIntent pendingIntent = PendingIntent. getBroadcast ( this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
        long futureInMillis = SystemClock. elapsedRealtime () + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE ) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
    }
    private Notification getNotification (String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle(title) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.drawable.ic_logo ) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }

    private void addEventListener(){
        _point = findViewById(R.id.linPoint);
        _unit = findViewById(R.id.linUnit);
        _pointBalerina = findViewById(R.id.imgBalerinaPoint);
        _unitBalerina = findViewById(R.id.imgBalerinaUnit);

        _point.setOnClickListener(this);
        _unit.setOnClickListener(this);
        findViewById(R.id.imgCalendar).setOnClickListener(this);

        findViewById(R.id.linCheckedBreakfast).setOnClickListener(this);
        findViewById(R.id.linCheckedLunch).setOnClickListener(this);
        findViewById(R.id.linCheckedDinner).setOnClickListener(this);
        findViewById(R.id.linCheckedSnackBreakfast).setOnClickListener(this);
        findViewById(R.id.linCheckedSnackLunch).setOnClickListener(this);
        findViewById(R.id.linCheckedSnackDinner).setOnClickListener(this);

        findViewById(R.id.linAddBreakfast).setOnClickListener(this);
        findViewById(R.id.linAddLunch).setOnClickListener(this);
        findViewById(R.id.linAddDinner).setOnClickListener(this);
        findViewById(R.id.linAddSnackBreakfast).setOnClickListener(this);
        findViewById(R.id.linAddSnackLunch).setOnClickListener(this);
        findViewById(R.id.linAddSnackDinner).setOnClickListener(this);
        findViewById(R.id.imgSubtractWeight).setOnClickListener(this);
        findViewById(R.id.imgAddWeight).setOnClickListener(this);

        findViewById(R.id.imgMain).setOnClickListener(this);
        findViewById(R.id.imgRecipe).setOnClickListener(this);
        findViewById(R.id.imgSetting).setOnClickListener(this);
        findViewById(R.id.imgHelp).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linPoint:
                _unit.setAlpha(0.5f);
                _unitBalerina.setVisibility(View.INVISIBLE);

                _point.setAlpha(1.0f);
                _pointBalerina.setVisibility(View.VISIBLE);
                break;
            case R.id.linUnit:
                _point.setAlpha(0.5f);
                _pointBalerina.setVisibility(View.INVISIBLE);

                _unit.setAlpha(1.0f);
                _unitBalerina.setVisibility(View.VISIBLE);
                break;
            case R.id.imgCalendar:
                showDatePickerDlg();
                break;
            case R.id.linCheckedBreakfast:
            {
                showMeal_List();
                Global.timing = "breakfast";
            }
                break;
            case R.id.linCheckedLunch:
            {
                showMeal_List();
                Global.timing = "lunch";
            }
            break;
            case R.id.linCheckedDinner:
            {
                showMeal_List();
                Global.timing = "dinner";
            }
            break;
            case R.id.linCheckedSnackBreakfast:
            {
                showMeal_List();
                Global.timing = "snack_breakfast";
            }
            break;
            case R.id.linCheckedSnackLunch:
            {
                Log.d( "onClick:","sdfafa");
                showMeal_List();
                Global.timing = "snack_lunch";
            }
            break;
            case R.id.linCheckedSnackDinner:
            {
                showMeal_List();
                Global.timing = "snack_dinner";
            }
            break;
            case R.id.linAddBreakfast: {
                searchFood();
                Global.timing = "breakfast";
            }
                break;
            case R.id.linAddLunch: {
                searchFood();
                Global.timing = "lunch";
            }
                break;
            case R.id.linAddDinner:
            {
                searchFood();
                Global.timing = "dinner";
            }
                break;
            case R.id.linAddSnackBreakfast:
            {
                searchFood();
                Global.timing = "snack_breakfast";
            }
                break;
            case R.id.linAddSnackLunch:
            {
                searchFood();
                Global.timing = "snack_lunch";
            }
                break;
            case R.id.linAddSnackDinner:
            {
                searchFood();
                Global.timing = "snack_dinner";
            }
                break;
            case R.id.imgSubtractWeight:
                float weight = Float.parseFloat(_currentWeight.getText().toString());
                weight -= 0.1;
                _currentWeight.setText(String.format(Locale.US, "%.1f", weight));
                break;
            case R.id.imgAddWeight:
                weight = Float.parseFloat(_currentWeight.getText().toString());
                weight += 0.1;
                _currentWeight.setText(String.format("%.1f", weight));
                break;
            case R.id.imgMain:
                break;
            case R.id.imgRecipe:
                Intent intent = new Intent(getBaseContext(), RecipesActivity.class);
                startActivity(intent);
                break;
            case R.id.imgSetting:
                intent = new Intent(getBaseContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.imgHelp:
                intent = new Intent(getBaseContext(), PolicyActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void showDatePickerDlg(){
        DatePickerDialog datePickerdlg = new DatePickerDialog(this, this,
                mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerdlg.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        mSelectedDate =  mCalendar.getTime();
        _date.setText(getDateString(mSelectedDate));
        _week.setText(getWeekNumber(mSelectedDate));
    }

    private String getDateString(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd");
        String dateString = formatter.format(date);

        DateTime today = new DateTime();
        DateTime tomorrow = today.plusDays(1);
        DateTime yesterday = today.minusDays(1);
        DateTime selected = new DateTime(date);

        if((selected.dayOfMonth().get() == today.dayOfMonth().get()) && (selected.monthOfYear().get() == today.monthOfYear().get()) && (selected.year().get() == today.year().get()))
            return "Σήμερα";
        if(selected.dayOfMonth().get() == yesterday.dayOfMonth().get() && selected.monthOfYear().get() == yesterday.monthOfYear().get() && selected.year().get() == yesterday.year().get())
            return "Εχθές";
        if(selected.dayOfMonth().get() == tomorrow.dayOfMonth().get() && selected.monthOfYear().get() == tomorrow.monthOfYear().get() && selected.year().get() == tomorrow.year().get())
            return "Αύριο";

        return dateString;
    }

    private String getWeekNumber(Date date){
        DateTime start = new DateTime(PersonalData.getInstance().getStart_date());
        DateTime end = new DateTime(date).plusDays(1);
        int weeknum = Weeks.weeksBetween(start, end).getWeeks();
        if(start.isAfter(end)){
            return "";
        }
        else{
            return "Εβδομάδα " + String.valueOf(weeknum + 1);
        }
    }

    private void showMeal_List(){
        Intent intent = new Intent(getBaseContext(), MealListActivity.class);
        intent.putExtra("yValue", y);
        startActivity(intent);
    }

    private void searchFood(){
        Intent intent = new Intent(getBaseContext(), SearchFoodActivity.class);
        intent.putExtra("yValue", y);
        startActivity(intent);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void initMeal(){
        openHelper = new MealDatabaseHelper(this);
        db = openHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT *FROM " + MealDatabaseHelper.TABLE_NAME,  null);
        initMeal_format();
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    String date = cursor.getString(cursor.getColumnIndex("date"));
                    if(date.equals(getCurrentDate())) {

                        String foodname = cursor.getString(cursor.getColumnIndex("foodname"));
                        String points_m = cursor.getString(cursor.getColumnIndex("points"));
                        String gram_m = cursor.getString(cursor.getColumnIndex("gram"));
                        String timing = cursor.getString(cursor.getColumnIndex("timing"));

                        float carbon = Float.parseFloat(cursor.getString(cursor.getColumnIndex("carbon")));
                        float protein = Float.parseFloat(cursor.getString(cursor.getColumnIndex("protein")));
                        float fat = Float.parseFloat(cursor.getString(cursor.getColumnIndex("fat")));
                        float points = Float.parseFloat(points_m);
                        float gram = Float.parseFloat(gram_m);

                        switch (timing){
                            case "breakfast" :
                            {
                                findViewById(R.id.linCheckedBreakfast).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddBreakfast).setVisibility(View.GONE);

                                Global.morning_carbon += carbon;
                                Global.morning_protein += protein;
                                Global.morning_fat += fat;
                                Global.morning_points += points;
                                Global.morning_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointBreakfast);
                                point_txt.setText((int)Global.morning_points+" points");

                                point_txt = findViewById(R.id.txtBreakfastProtein);
                                point_txt.setText("Protein : "+(int)(Global.morning_protein/Global.morning_total*100)+"%");

                                point_txt = findViewById(R.id.txtBreakfastCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.morning_carbon/Global.morning_total*100)+"%");

                                point_txt = findViewById(R.id.txtBreakfastFat);
                                point_txt.setText("Fat : "+(int)(Global.morning_fat/Global.morning_total*100)+"%");
                            }
                            break;
                            case "lunch" :
                            {
                                findViewById(R.id.linCheckedLunch).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddLunch).setVisibility(View.GONE);

                                Global.lunch_carbon += carbon;
                                Global.lunch_protein += protein;
                                Global.lunch_fat += fat;
                                Global.lunch_points += points;
                                Global.lunch_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointLunch);
                                point_txt.setText((int)Global.lunch_points+" points");

                                point_txt = findViewById(R.id.txtLunchProtein);
                                point_txt.setText("Protein : "+(int)(Global.lunch_protein/Global.lunch_total*100)+"%");

                                point_txt = findViewById(R.id.txtLunchCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.lunch_carbon/Global.lunch_total*100)+"%");

                                point_txt = findViewById(R.id.txtLunchFat);
                                point_txt.setText("Fat : "+(int)(Global.lunch_fat/Global.lunch_total*100)+"%");
                            }
                            break;
                            case "dinner" :
                            {
                                findViewById(R.id.linCheckedDinner).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddDinner).setVisibility(View.GONE);

                                Global.dinner_carbon += carbon;
                                Global.dinner_protein += protein;
                                Global.dinner_fat += fat;
                                Global.dinner_points += points;
                                Global.dinner_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointDinner);
                                point_txt.setText((int)Global.dinner_points+" points");

                                point_txt = findViewById(R.id.txtDinnerProtein);
                                point_txt.setText("Protein : "+(int)(Global.dinner_protein/Global.dinner_total*100)+"%");

                                point_txt = findViewById(R.id.txtDinnerCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.dinner_carbon/Global.dinner_total*100)+"%");

                                point_txt = findViewById(R.id.txtDinnerFat);
                                point_txt.setText("Fat : "+(int)(Global.dinner_fat/Global.dinner_total*100)+"%");
                            }
                            break;
                            case "snack_breakfast" :
                            {
                                findViewById(R.id.linCheckedSnackBreakfast).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddSnackBreakfast).setVisibility(View.GONE);

                                Global.snack_morning_carbon += carbon;
                                Global.snack_morning_protein += protein;
                                Global.snack_morning_fat += fat;
                                Global.snack_morning_points += points;
                                Global.snack_morning_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointSnackBreakfast);
                                point_txt.setText((int)Global.snack_morning_points+" points");

                                point_txt = findViewById(R.id.txtSnackBreakfastProtein);
                                point_txt.setText("Protein : "+(int)(Global.snack_morning_protein/Global.snack_morning_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackBreakfastCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.snack_morning_carbon/Global.snack_morning_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackBreakfastFat);
                                point_txt.setText("Fat : "+(int)(Global.snack_morning_fat/Global.snack_morning_total*100)+"%");
                            }
                            break;
                            case "snack_lunch" :
                            {
                                findViewById(R.id.linCheckedSnackLunch).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddSnackLunch).setVisibility(View.GONE);

                                Global.snack_lunch_carbon += carbon;
                                Global.snack_lunch_protein += protein;
                                Global.snack_lunch_fat += fat;
                                Global.snack_lunch_points += points;
                                Global.snack_lunch_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointSnackLunch);
                                point_txt.setText((int)(Global.snack_lunch_points)+" points");

                                point_txt = findViewById(R.id.txtSnackLunchProtein);
                                point_txt.setText("Protein : "+(int)(Global.snack_lunch_protein/Global.snack_lunch_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackLunchCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.snack_lunch_carbon/Global.snack_lunch_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackLunchFat);
                                point_txt.setText("Fat : "+(int)(Global.snack_lunch_fat/Global.snack_lunch_total*100)+"%");
                            }
                            break;
                            case "snack_dinner" :
                            {
                                findViewById(R.id.linCheckedSnackDinner).setVisibility(View.VISIBLE);
                                findViewById(R.id.linAddSnackDinner).setVisibility(View.GONE);

                                Global.snack_dinner_carbon += carbon;
                                Global.snack_dinner_protein += protein;
                                Global.snack_dinner_fat += fat;
                                Global.snack_dinner_points += points;
                                Global.snack_dinner_total +=gram;

                                TextView point_txt = findViewById(R.id.txtPointSnackDinner);
                                point_txt.setText((int)Global.snack_dinner_points+" points");

                                point_txt = findViewById(R.id.txtSnackDinnerProtein);
                                point_txt.setText("Protein : "+(int)(Global.snack_dinner_protein/Global.snack_dinner_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackDinnerCarbon);
                                point_txt.setText("Carbon : "+(int)(Global.snack_dinner_carbon/Global.snack_dinner_total*100)+"%");

                                point_txt = findViewById(R.id.txtSnackDinnerFat);
                                point_txt.setText("Fat : "+(int)(Global.snack_dinner_fat/Global.snack_dinner_total*100)+"%");
                            }
                            break;
                        }
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();

            SQLiteDatabase db_personal;
            SQLiteOpenHelper openHelper_persoanl;
            openHelper_persoanl = new PersonalDatabaseHelper(this);
            db_personal = openHelper_persoanl.getWritableDatabase();

            String start_date = getCurrentDate();
            String weekly_units = PersonalData.getInstance().getUnits()+"";

            String date = getCurrentDate();

            DateTimeFormatter df = DateTimeFormat.forPattern("dd/MM/yyyy");
            DateTime any_date = DateTime.parse(getCurrentDate(), df);
            DateTime yesterday = any_date.minusDays(1);
            DateTime st_date = DateTime.parse(getCurrentDate(), df);

            SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
            String yesterday_s = ddf.format(yesterday.toDate());
            float weight_average = PersonalData.getInstance().getWeight();
            Integer weight_n = 0;

            final Cursor cursor1 = db_personal.rawQuery("SELECT *FROM " + PersonalDatabaseHelper.TABLE_NAME ,  null);
            if(cursor1 != null) {
                if (cursor1.moveToFirst()) {
                    do {
                        Integer id = cursor1.getInt(cursor1.getColumnIndex("ID"));

                        if (id == 1)
                            start_date = cursor1.getString(cursor1.getColumnIndex("date"));

                        Integer day_s = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("days")));

                        if (yesterday_s.equals(cursor1.getString(cursor1.getColumnIndex("date")))) {
                            weekly_units = "" + (Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("weekly_units"))) + cursor1.getInt(cursor1.getColumnIndex("next_units")));
                        }

                        if (day_s > 7) {
                            Integer n = (day_s / 7);
                            if (day_s > 7 * (n - 1) && day_s <= 7 * n) {
                                weight_n++;
                                weight_average += PersonalData.getInstance().getWeight();
                            }
                        }
                    } while (cursor1.moveToNext());
                }
                cursor1.close();

                if(weight_n>0)
                weight_average = weight_average / weight_n;
                st_date = DateTime.parse(start_date, df);

            }

            Float weight_f = PersonalData.getInstance().getWeight();
            Float height_f = PersonalData.getInstance().getHeight();
            Float waist_f = PersonalData.getInstance().getWaist_perimeter();
            Float neck_f = PersonalData.getInstance().getNeck_perimeter();
            Float thigh_f = PersonalData.getInstance().getThigh_perimeter();

            Integer daily_units = (int) ((Global.morning_points + Global.lunch_points + Global.dinner_points + Global.snack_morning_points + Global.snack_lunch_points + Global.snack_dinner_points) / 1.19394);

            String days = (any_date.getDayOfYear() - st_date.getDayOfYear()) + 1 + "";

            if(Integer.parseInt(days)%7==1)
            {
                weekly_units = PersonalData.getInstance().getUnits()+"";
            }
            Integer next_units = (Integer.parseInt(weekly_units)-daily_units);

           insertPersonalData(weight_f+"", height_f+"", waist_f+"", neck_f+"", thigh_f+"", weekly_units, daily_units+"", date, days, next_units, Global.user_id);

            TextView deltaWeight = findViewById(R.id.deltaWeight);
            TextView deltaUnits = findViewById(R.id.deltaUnits);
            TextView loseAlert = findViewById(R.id.loseAlert);

            float delta_weight = weight_f - weight_average;

            if(delta_weight<0) {
                deltaWeight.setText(delta_weight + "");
                loseAlert.setVisibility(View.VISIBLE);
            }
            else {
                deltaWeight.setText("--");
                loseAlert.setVisibility(View.INVISIBLE);
            }

                deltaUnits.setText(""+next_units);

            TextView _pointValue = findViewById(R.id.txtPoint);
            TextView _unitValue = findViewById(R.id.txtUnit);
            _unitValue.setText(weekly_units);
            _pointValue.setText((int)(Float.parseFloat(weekly_units)*1.19394)+"");
        }
    }

    public void insertPersonalData(String weight, String height, String waist, String neck, String thigh, String weekly_units, String daily_units, String date, String days, Integer next_units, String user_id)
    {
        SQLiteDatabase db_personal;
        SQLiteOpenHelper openHelper_persoanl;
        openHelper_persoanl = new PersonalDatabaseHelper(this);
        db_personal = openHelper_persoanl.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PersonalDatabaseHelper.COL_2, weight);
        contentValues.put(PersonalDatabaseHelper.COL_3, height);
        contentValues.put(PersonalDatabaseHelper.COL_4, waist);
        contentValues.put(PersonalDatabaseHelper.COL_5, neck);
        contentValues.put(PersonalDatabaseHelper.COL_6, thigh);
        contentValues.put(PersonalDatabaseHelper.COL_7, weekly_units);
        contentValues.put(PersonalDatabaseHelper.COL_8, daily_units);
        contentValues.put(PersonalDatabaseHelper.COL_9, date);
        contentValues.put(PersonalDatabaseHelper.COL_10, days);
        contentValues.put(PersonalDatabaseHelper.COL_11, next_units);
        contentValues.put(PersonalDatabaseHelper.COL_12, user_id);
        final Cursor cursor = db_personal.rawQuery("SELECT *FROM " + PersonalDatabaseHelper.TABLE_NAME ,  null);

        db_personal.insert(PersonalDatabaseHelper.TABLE_NAME, null, contentValues);
    }

    private void initMeal_format(){
        findViewById(R.id.linCheckedBreakfast).setVisibility(View.GONE);
        findViewById(R.id.linAddBreakfast).setVisibility(View.VISIBLE);
        TextView point_txt = findViewById(R.id.txtPointBreakfast);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtBreakfastProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtBreakfastCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtBreakfastFat);
        point_txt.setText("");

        findViewById(R.id.linCheckedLunch).setVisibility(View.GONE);
        findViewById(R.id.linAddLunch).setVisibility(View.VISIBLE);
        point_txt = findViewById(R.id.txtPointLunch);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtLunchProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtLunchCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtLunchFat);
        point_txt.setText("");

        findViewById(R.id.linCheckedDinner).setVisibility(View.GONE);
        findViewById(R.id.linAddDinner).setVisibility(View.VISIBLE);
        point_txt = findViewById(R.id.txtPointDinner);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtDinnerProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtDinnerCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtDinnerFat);
        point_txt.setText("");

        findViewById(R.id.linCheckedSnackBreakfast).setVisibility(View.GONE);
        findViewById(R.id.linAddSnackBreakfast).setVisibility(View.VISIBLE);
        point_txt = findViewById(R.id.txtPointSnackBreakfast);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtSnackBreakfastProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackBreakfastCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackDinnerFat);
        point_txt.setText("");

        findViewById(R.id.linCheckedSnackLunch).setVisibility(View.GONE);
        findViewById(R.id.linAddSnackLunch).setVisibility(View.VISIBLE);
        point_txt = findViewById(R.id.txtPointSnackLunch);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtSnackLunchProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackLunchCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackLunchFat);
        point_txt.setText("");

        findViewById(R.id.linCheckedSnackDinner).setVisibility(View.GONE);
        findViewById(R.id.linAddSnackDinner).setVisibility(View.VISIBLE);
        point_txt = findViewById(R.id.txtPointSnackDinner);
        point_txt.setText(("   points"));

        point_txt = findViewById(R.id.txtSnackDinnerProtein);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackDinnerCarbon);
        point_txt.setText("");

        point_txt = findViewById(R.id.txtSnackDinnerFat);
        point_txt.setText("");

        Global.morning_protein = 0;
        Global.morning_carbon = 0;
        Global.morning_fat = 0;
        Global.morning_points = 0;
        Global.morning_total = 0;

        Global.lunch_points=0;
        Global.lunch_total=0;
        Global.lunch_fat=0;
        Global.lunch_protein=0;
        Global.lunch_carbon=0;

        Global.dinner_protein = 0;
        Global.dinner_carbon = 0;
        Global.dinner_fat = 0;
        Global.dinner_points = 0;
        Global.dinner_total = 0;

        Global.snack_morning_points =0;
        Global.snack_morning_total = 0;
        Global.snack_morning_fat = 0;
        Global.snack_morning_protein = 0;
        Global.snack_morning_carbon = 0;

        Global.snack_lunch_protein =0;
        Global.snack_lunch_carbon =0;
        Global.snack_lunch_fat =0;
        Global.snack_lunch_points =0;
        Global.snack_lunch_total =0;

        Global.snack_dinner_protein=0;
        Global.snack_dinner_carbon=0;
        Global.snack_dinner_fat=0;
        Global.snack_dinner_points=0;
        Global.snack_dinner_total =0;
    }

    private void graphSeries(){
        SQLiteDatabase db_personal;
        SQLiteOpenHelper openHelper_persoanl;
        openHelper_persoanl = new PersonalDatabaseHelper(this);
        db_personal = openHelper_persoanl.getWritableDatabase();

        final Cursor cursor1 = db_personal.rawQuery("SELECT *FROM " + PersonalDatabaseHelper.TABLE_NAME ,  null);

        Integer days =-1;
        float weights = 0;
        if(cursor1 != null) {
            if (cursor1.moveToFirst()) {
                do {
                    Integer days1 = Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("days")));
                    if(days!=days1) {
                        weights = Float.parseFloat(cursor1.getString(cursor1.getColumnIndex("weight")));
                        series.appendData(new DataPoint(days1, weights), true, 100);
                    }
                    days=days1;
                } while (cursor1.moveToNext());
            }
            cursor1.close();
        }
    }
}