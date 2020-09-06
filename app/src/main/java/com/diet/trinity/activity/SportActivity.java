package com.diet.trinity.activity;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.diet.trinity.R;
import com.diet.trinity.model.PersonalData;

public class SportActivity extends AppCompatActivity {

    RadioGroup _gym;
    int mGym = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport);
        addEventListener();
    }

    private void addEventListener(){
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalData.getInstance().setGymType(mGym);

                Intent intent = new Intent(getBaseContext(), IdealWeightActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        _gym = findViewById(R.id.radGym);
        final LinearLayout _sportContainer = findViewById(R.id.linSportContainer);
        _gym.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mGym = checkedId % 5 - 1;
//                Toast.makeText(getBaseContext(), String.valueOf(mGym), Toast.LENGTH_LONG).show();
                if(mGym == -1){
                    mGym = 4;
                    _sportContainer.setVisibility(View.VISIBLE);
                }
                else{
                    _sportContainer.setVisibility(View.GONE);
                }
            }
        });
    }
}