package com.diet.trinity.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.diet.trinity.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.diet.trinity.Adapter.CustomAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.android.volley.toolbox.Volley;
import com.diet.trinity.Utility.DatabaseHelper;
import com.diet.trinity.Utility.Common;
import com.diet.trinity.Utility.FoodDatabaseHelper;
import com.diet.trinity.Utility.PersonalDatabaseHelper;
import com.diet.trinity.model.Goal;
import com.diet.trinity.model.Listmodel;
import com.diet.trinity.model.PersonalData;
import com.diet.trinity.Utility.MealDatabaseHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.net.HttpURLConnection;
import com.diet.trinity.Utility.Global;

public class SearchFoodActivity extends AppCompatActivity {
    ArrayList<Listmodel> Listitem=new ArrayList<Listmodel>();
    private SQLiteDatabase db, db1;
    private SQLiteOpenHelper openHelper, openHelper1;
    RelativeLayout loading;
    ListView listView;
    String  foodname, carbon_m, protein_m, fat_m, gram_m;
    int id;
    CustomAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_food);


        addEventListener();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        try {
            reload();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addEventListener(){
        final TextView _count = findViewById(R.id.txtSelectedMealCount);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                //finish();
                Intent intent = new Intent(SearchFoodActivity.this, DailyCaleandarActivity.class);
                intent.putExtra("yPos", getIntent().getIntExtra("yValue",0));
                startActivity(intent);
            }
        });

        listView = findViewById(R.id.mainlistview );
    }

    public void addFoodNumber(View v){
        final TextView _count = findViewById(R.id.txtSelectedMealCount);
        int count = Integer.parseInt(_count.getText().toString()) + 1;
        _count.setText(String.valueOf(count));
        Goal goal = PersonalData.getInstance().getGoal();
        int no = v.getId();
        openHelper = new FoodDatabaseHelper(this);
        db = openHelper.getWritableDatabase();

        openHelper1 = new MealDatabaseHelper(this);
        db1 = openHelper1.getWritableDatabase();
//-----------add food save----------//
        final Cursor cursor = db.rawQuery("SELECT *FROM " + FoodDatabaseHelper.TABLE_NAME ,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    if(no==cursor.getInt(cursor.getColumnIndex("ID")))
                    {
                        Global.carbon = Float.parseFloat(cursor.getString(cursor.getColumnIndex("carbon")));
                        Global.protein = Float.parseFloat(cursor.getString(cursor.getColumnIndex("protein")));
                        Global.fat = Float.parseFloat(cursor.getString(cursor.getColumnIndex("fat")));
                        Global.points = (Global.carbon+Global.protein+Global.fat)/15;

                        Global.total +=(Global.carbon+Global.protein+Global.fat);

                        Global.categoryid = Integer.parseInt(cursor.getString(cursor.getColumnIndex("categoryid")));

                        foodname = cursor.getString(cursor.getColumnIndex("foodname"));
                        carbon_m = cursor.getString(cursor.getColumnIndex("carbon"));
                        protein_m = cursor.getString(cursor.getColumnIndex("protein"));
                        fat_m = cursor.getString(cursor.getColumnIndex("fat"));
                        gram_m = cursor.getString(cursor.getColumnIndex("gram"));
                        String points_m = Global.points+"";
                        String categoryid = Global.categoryid+"";
                        String date = getCurrentDate();
                        String timing  = Global.timing;

                        insertData(foodname,carbon_m,protein_m,fat_m,gram_m,points_m,categoryid,timing,date);

                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
    }

    private void reload() throws JSONException {
        //loading.setVisibility(View.VISIBLE);

        openHelper = new FoodDatabaseHelper(this);
        db = openHelper.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT *FROM " + FoodDatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            Log.d("reload: ", String.valueOf(cursor.getCount()));
            if (cursor.moveToFirst()){
                do{
                    id=cursor.getInt(cursor.getColumnIndex("ID"));
                    foodname = cursor.getString(cursor.getColumnIndex("foodname"));
                    carbon_m = cursor.getString(cursor.getColumnIndex("carbon"));
                    protein_m = cursor.getString(cursor.getColumnIndex("protein"));
                    fat_m = cursor.getString(cursor.getColumnIndex("fat"));
                    gram_m = cursor.getString(cursor.getColumnIndex("gram"));
                    Listitem.add(new Listmodel(id,foodname, carbon_m, protein_m,fat_m,gram_m));
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        Log.d("reload: ", String.valueOf(cursor.getCount()));
        myAdapter=new CustomAdapter(SearchFoodActivity.this, Listitem);
        listView.setAdapter(myAdapter);
    }

    public void insertData(String foodname,String carbon_m, String protein_m, String fat_m, String gram_m,String points_m, String category_id,String timing, String date){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MealDatabaseHelper.COL_2, foodname);
        contentValues.put(MealDatabaseHelper.COL_3, carbon_m);
        contentValues.put(MealDatabaseHelper.COL_4, protein_m);
        contentValues.put(MealDatabaseHelper.COL_5, fat_m);
        contentValues.put(MealDatabaseHelper.COL_6, gram_m);
        contentValues.put(MealDatabaseHelper.COL_7, points_m);
        contentValues.put(MealDatabaseHelper.COL_8, category_id);
        contentValues.put(MealDatabaseHelper.COL_9, timing);
        contentValues.put(MealDatabaseHelper.COL_10, date);
        db1.insert(MealDatabaseHelper.TABLE_NAME,null,contentValues);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
