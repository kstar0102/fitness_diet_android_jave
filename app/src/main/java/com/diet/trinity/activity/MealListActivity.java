package com.diet.trinity.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diet.trinity.Adapter.CustomAdapter;
import com.diet.trinity.Adapter.CustomMealAdapter;
import com.diet.trinity.R;
import com.diet.trinity.Utility.FoodDatabaseHelper;
import com.diet.trinity.Utility.Global;
import com.diet.trinity.Utility.MealDatabaseHelper;
import com.diet.trinity.model.Listmodel;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MealListActivity extends AppCompatActivity {
    ArrayList<Listmodel> Listitem=new ArrayList<Listmodel>();
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;
    ListView listView;
    String  foodname, carbon_m, protein_m, fat_m, gram_m;
    int id;
    CustomMealAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_list);
        try {
            initMeal();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addEventListener();
    }

    private void addEventListener() {
        final TextView _count = findViewById(R.id.txtSelectedMealCount);

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void initMeal() throws JSONException {
        openHelper = new MealDatabaseHelper(this);
        db = openHelper.getWritableDatabase();
        listView = findViewById(R.id.listview);

        final Cursor cursor = db.rawQuery("SELECT *FROM " + MealDatabaseHelper.TABLE_NAME,  null);
        if(cursor != null){
            Log.d("reload: ", String.valueOf(cursor.getCount()));
            if (cursor.moveToFirst()){
                do{
                    String date  = cursor.getString(cursor.getColumnIndex("date"));
                    String timing = cursor.getString(cursor.getColumnIndex("timing"));
                    if(date.equals(getCurrentDate()) && timing.equals(Global.timing)) {
                        id = cursor.getInt(cursor.getColumnIndex("ID"));
                        foodname = cursor.getString(cursor.getColumnIndex("foodname"));
                        carbon_m = cursor.getString(cursor.getColumnIndex("carbon"));
                        protein_m = cursor.getString(cursor.getColumnIndex("protein"));
                        fat_m = cursor.getString(cursor.getColumnIndex("fat"));
                        gram_m = cursor.getString(cursor.getColumnIndex("gram"));
                        Listitem.add(new Listmodel(id, foodname, carbon_m, protein_m, fat_m, gram_m));
                    }
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        myAdapter=new CustomMealAdapter(MealListActivity.this, Listitem);
        listView.setAdapter(myAdapter);
    }

    public String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}