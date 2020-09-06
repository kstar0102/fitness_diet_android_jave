package com.diet.trinity.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.diet.trinity.R;

public class TrialNotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trial_notify);

        addEventListener();
    }

    private void addEventListener(){
        final RelativeLayout _trial = findViewById(R.id.linTrial);
        RelativeLayout _premium = findViewById(R.id.linPremium);
        TabLayout _tab = findViewById(R.id.tabMembership);
        _tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    _trial.setVisibility(View.VISIBLE);
                }
                else{
                    _trial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DailyCaleandarActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imgNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), DailyCaleandarActivity.class);
                startActivity(intent);
            }
        });
    }
}