package com.diet.trinity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.diet.trinity.R;
import com.diet.trinity.model.DietMode;
import com.diet.trinity.model.PersonalData;

public class WelcomeActivity extends AppCompatActivity {

    TextView _point, _unit;
    int mPoint, mUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        _point = findViewById(R.id.txtPoint);
        _unit = findViewById(R.id.txtUnit);

        addEventListener();
        initView();
    }

    private void initView(){
        mUnit = PersonalData.getInstance().getUnits();
        mPoint = PersonalData.getInstance().getPoints();

        _point.setText(String.valueOf(mPoint));
        _unit.setText(String.valueOf(mUnit));
    }

    private void addEventListener(){
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonalData.getInstance().writeData(getBaseContext());

                Intent intent = new Intent(getBaseContext(), TrialNotifyActivity.class);
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

        final LinearLayout _iconPoint = findViewById(R.id.linPoint);
        final LinearLayout _iconUnit = findViewById(R.id.linUnit);
        _iconPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _iconPoint.setAlpha(0.5f);
                _iconUnit.setAlpha(1.0f);
                PersonalData.getInstance().setDietMode(DietMode.POINT);

            }
        });

        _iconUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _iconPoint.setAlpha(1.0f);
                _iconUnit.setAlpha(0.5f);
                PersonalData.getInstance().setDietMode(DietMode.UNIT);
            }
        });
    }
}