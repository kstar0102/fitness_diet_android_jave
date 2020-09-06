package com.diet.trinity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diet.trinity.Adapter.CustomAdapter;
import com.diet.trinity.Utility.FoodDatabaseHelper;
import com.diet.trinity.Utility.Global;
import com.diet.trinity.activity.BirthdayActivity;
import com.diet.trinity.activity.DailyCaleandarActivity;
import com.diet.trinity.activity.GoalActivity;
import com.diet.trinity.activity.PolicyActivity;
import com.diet.trinity.activity.RecipesActivity;
import com.diet.trinity.activity.TrialNotifyActivity;
import com.diet.trinity.activity.LineChartActivity;
import com.diet.trinity.activity.RegisterActivity;
import com.diet.trinity.activity.LoginActivity;

import com.diet.trinity.model.Goal;
import com.diet.trinity.model.PersonalData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.diet.trinity.Utility.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db, db1;
    private SQLiteOpenHelper openHelper, openHelper1;
    String token, result, foodname, carbon_m, protein_m, fat_m, gram_m, category_id;
    ArrayList Listitem = new ArrayList<>();
    JSONArray fooditems = new JSONArray();
    CustomAdapter myAdapter;
    RequestQueue queue;
    ArrayList<String> data = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        },2000);

        openHelper = new DatabaseHelper(this);
        db = openHelper.getWritableDatabase();

        load_food();

    }

    private void moveToMembership(){
        Intent intent = new Intent(getBaseContext(), TrialNotifyActivity.class);
        startActivity(intent);
    }

    private void moveToDaily(){
        Intent intent = new Intent(getBaseContext(), DailyCaleandarActivity.class);
        startActivity(intent);
    }

    private void moveToGoal(){
        Intent intent = new Intent(getBaseContext(), GoalActivity.class);
        startActivity(intent);
    }

    private void moveToRegister(){
        Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
        startActivity(intent);
    }

    private void moveToLogin(){
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
    }


    private void loadData(){
        PersonalData myData;

        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        try {
            InputStream inputStream = this.openFileInput("personal.db");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String json_string = bufferedReader.readLine();

                Gson gson = new Gson();
                Type listType = new TypeToken<PersonalData>() {
                }.getType();
                myData = gson.fromJson(json_string, listType);
                PersonalData.setInstance(myData);

                if(myData.getMembership() == 0){
                    moveToMembership();
                }
                else{
                    moveToDaily();
                }
            }
        }
        catch (FileNotFoundException e) {
            moveToLogin();
        } catch (IOException e) {
            moveToLogin();
        }

    }

    private void load_food()
    {
        openHelper1 = new FoodDatabaseHelper(this);
        db1 = openHelper1.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        db1.execSQL("delete from "+FoodDatabaseHelper.TABLE_NAME);

        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    token = cursor.getString(cursor.getColumnIndex("token"));
                    Global.token = token;
                }while(cursor.moveToNext());
            }
            cursor.close();
        }

        String foodUrl = "http://192.168.109.36/api/fooditems";

        StringRequest postRequest = new StringRequest(Request.Method.GET, foodUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            fooditems = jsonObject.optJSONArray("fooditems");
                            result = jsonObject.getString("success");

                            if (result.equals("true")){
                                for(int i=0;i<fooditems.length();i++) {
                                    foodname = fooditems.getJSONObject(i).getString("food_name");
                                    carbon_m = fooditems.getJSONObject(i).getString("carbon");
                                    protein_m = fooditems.getJSONObject(i).getString("protein");
                                    fat_m = fooditems.getJSONObject(i).getString("fat");
                                    gram_m = fooditems.getJSONObject(i).getString("portion_in_grams");
                                    category_id = fooditems.getJSONObject(i).getString("food_categories_id");
                                    insertData(i,foodname,carbon_m,protein_m,fat_m,gram_m,category_id);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.retry_info_login), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String> headers = new HashMap<>();
                Log.d("getHeaders: ", token);
                headers.put("Authorization", "Bearer "+token);
                return headers;
            }
        };
        queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(postRequest);
    }

    public void insertData(Integer id, String foodname,String carbon_m, String protein_m, String fat_m, String gram_m, String category_id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FoodDatabaseHelper.COL_1, id);
        contentValues.put(FoodDatabaseHelper.COL_2, foodname);
        contentValues.put(FoodDatabaseHelper.COL_3, carbon_m);
        contentValues.put(FoodDatabaseHelper.COL_4, protein_m);
        contentValues.put(FoodDatabaseHelper.COL_5, fat_m);
        contentValues.put(FoodDatabaseHelper.COL_6, gram_m);
        contentValues.put(FoodDatabaseHelper.COL_7, category_id);
        db1.insert(FoodDatabaseHelper.TABLE_NAME,null,contentValues);
    }
}
