package com.diet.trinity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.diet.trinity.R;
import com.diet.trinity.model.PersonalData;

public class HeightActivity extends AppCompatActivity {

    EditText _height;
    int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_height);

        addEventListener();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void addEventListener(){
        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    PersonalData.getInstance().setHeight(mHeight);

                    Intent intent = new Intent(getBaseContext(), BirthdayActivity.class);
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
        _height = findViewById(R.id.editHeight);

        try{
            mHeight = Integer.parseInt(_height.getText().toString());
        }
        catch (Exception e){
            mHeight = 0;
        }

        if(mHeight <= 30){
            _height.setError("εισάγετε ύψος");
            validate = false;
        }
        else{
            _height.setError(null);
        }
        return validate;
    }
}