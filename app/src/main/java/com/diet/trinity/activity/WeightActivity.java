package com.diet.trinity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.diet.trinity.R;
import com.diet.trinity.model.PersonalData;

public class WeightActivity extends AppCompatActivity {
    EditText _weight;
    float mWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        //Toast.makeText(this, String.valueOf(PersonalData.getInstance().getGoal()), Toast.LENGTH_LONG).show();
        addEventListener();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void addEventListener(){
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    PersonalData.getInstance().setWeight(mWeight);
                    PersonalData.getInstance().setInitial_weight(mWeight);

                    Intent intent = new Intent(getBaseContext(), GenderActivity.class);
                    startActivity(intent);
                }
            }
        });

        findViewById(R.id.imgBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private boolean validate(){
        boolean validate = true;
        _weight = findViewById(R.id.editWeight);

        try{
            mWeight = Float.parseFloat(_weight.getText().toString());
        }
        catch (Exception e){
            mWeight = 0.0f;
        }

        if(mWeight <= 1.0f){
            _weight.setError("εισάγετε Βάρος");
            validate = false;
        }
        else{
            _weight.setError(null);
        }
        return validate;
    }
}