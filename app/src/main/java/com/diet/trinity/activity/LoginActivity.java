package com.diet.trinity.activity;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import com.diet.trinity.R;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.diet.trinity.Utility.Common;
import com.diet.trinity.Utility.DatabaseHelper;
import com.diet.trinity.Utility.FoodDatabaseHelper;
import com.diet.trinity.Utility.Global;

public class LoginActivity extends AppCompatActivity {
    private SQLiteDatabase db, db1;
    private SQLiteOpenHelper openHelper, openHelper1;
    EditText email, pass;
    ImageButton loginBtn;
    String Email, Pass, result, user_id, token, useremail, userpass;
    RequestQueue queue;
    ImageView already_account, goRegister, img_back;
    JSONArray fooditems = new JSONArray();
    String foodname, carbon_m, protein_m, fat_m, gram_m, category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        openHelper = new DatabaseHelper(this);
        db = openHelper.getWritableDatabase();

        final Cursor cursor = db.rawQuery("SELECT *FROM " + DatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            if (cursor.moveToFirst()){
                do{
                    useremail = cursor.getString(cursor.getColumnIndex("email"));
                    userpass = cursor.getString(cursor.getColumnIndex("password"));
                    Global.user_id = cursor.getString(cursor.getColumnIndex("user_id"));
                    System.out.println(useremail);
                    System.out.println(userpass);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        System.out.println(cursor);

        email = findViewById(R.id.EmailAddress);
        pass = findViewById(R.id.Password);
        already_account = findViewById(R.id.alreadyaccount);
        loginBtn = findViewById(R.id.RegisterButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString().trim();
                Pass = pass.getText().toString().trim();
                if (Email.isEmpty() || Pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.entry_info_login), Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    ApiLogin();
                }
            }
        });

        goRegister = findViewById(R.id.goRegister);
        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        img_back = findViewById(R.id.imgBack);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void locallogin(){
        if(Email.equals(useremail) && Pass.equals(userpass)){
            Intent intent = new Intent(LoginActivity.this, GoalActivity.class);
            startActivity(intent);
        }else {
            Toast.makeText(LoginActivity.this, "It is currently offline or wrong information.", Toast.LENGTH_SHORT).show();
        }
    }

    private void ApiLogin() {
        String url = Common.getInstance().getLoginUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            user_id = jsonObject.getString("user_id");
                            token = jsonObject.getString("accessToken");
                            if (result.equals("true")){
                                insertData(Email, Pass, user_id, token);
                                load_food();
                                Intent intent = new Intent(LoginActivity.this, GoalActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.retry_info_login), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        locallogin();
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", Email);
                params.put("password", Pass);
                params.put("c_password", Pass);
                params.put("gender", "0");
                params.put("birthday", "1999-09-09");
                params.put("type", "0");
                return params;
            }
        };
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(postRequest);
    }

    public void insertData(String femail,String fpassword, String fuser_id, String ftoken){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COL_2,femail);
        contentValues.put(DatabaseHelper.COL_3,fpassword);
        contentValues.put(DatabaseHelper.COL_4, fuser_id);
        contentValues.put(DatabaseHelper.COL_5, ftoken);

        db.insert(DatabaseHelper.TABLE_NAME,null,contentValues);
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
                                    insertData1(i,foodname,carbon_m,protein_m,fat_m,gram_m,category_id);
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.retry_info_login), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.offline_text), Toast.LENGTH_LONG).show();
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
        queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(postRequest);
    }

    public void insertData1(Integer id, String foodname,String carbon_m, String protein_m, String fat_m, String gram_m, String category_id){
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