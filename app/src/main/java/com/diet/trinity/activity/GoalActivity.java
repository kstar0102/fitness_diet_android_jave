package com.diet.trinity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.diet.trinity.R;
import com.diet.trinity.model.Goal;
import com.diet.trinity.model.PersonalData;

public class GoalActivity extends AppCompatActivity implements View.OnClickListener {
private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        findViewById(R.id.imgLose).setOnClickListener(this);
        findViewById(R.id.imgGain).setOnClickListener(this);
        findViewById(R.id.imgStay).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgLose :
                PersonalData.getInstance().setGoal(Goal.LOSE);
                //intent = new Intent(this, MySearchActivity.class);
                //startActivity(intent);
                //
                break;
            case R.id.imgGain :
                PersonalData.getInstance().setGoal(Goal.GAIN);
                //intent = new Intent(this, PaypalLinkActivity.class);
                //startActivity(intent);
                break;
            case R.id.imgStay :
                PersonalData.getInstance().setGoal(Goal.STAY);
                break;
        }
        Intent intent = new Intent(this, WeightActivity.class);
        //Intent intent = new Intent(this, MySearchActivity.class);
        startActivity(intent);
    }
}