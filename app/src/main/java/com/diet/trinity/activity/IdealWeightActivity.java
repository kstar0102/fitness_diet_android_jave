package com.diet.trinity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.diet.trinity.Common;
import com.diet.trinity.R;
import com.diet.trinity.model.Goal;
import com.diet.trinity.model.PersonalData;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import org.w3c.dom.Text;

public class IdealWeightActivity extends AppCompatActivity {

    EditText _weight;
    TextView _warning;
    ImageView _warningImage;
    IndicatorSeekBar _weeklyReduce;
    int mWeeklyReduce = 300;
    float mGoalWeight;
    int mIdealWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ideal_weight);

        _weight = findViewById(R.id.editWeight);
        _warning = findViewById(R.id.txtWarning);
        _warningImage = findViewById(R.id.imgWarning);
        _weeklyReduce = findViewById(R.id.seekWeeklyReduce);

        addEventListener();
        initView();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initView(){
        mIdealWeight = (int) PersonalData.getInstance().getIdeal_weight();
        TextView _idealWeight = findViewById(R.id.txtIdealWeight);
        _idealWeight.setText("Με βάση τις απαντήσεις σου, το ιδανικό βάρος σου είναι: " + mIdealWeight + " κιλά");

        if(PersonalData.getInstance().getGoal() != Goal.LOSE){
            _weeklyReduce.setVisibility(View.GONE);
        }
    }

    private void addEventListener(){
        _weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    mGoalWeight = Float.parseFloat(s.toString());
                }
                catch (Exception e){
                    mGoalWeight = 0.0f;
                }

                float percentage = mGoalWeight / mIdealWeight;
                if(percentage < 0.95){ // || percentage > 1.05){
                    _warning.setVisibility(View.VISIBLE);
                    _warningImage.setVisibility(View.VISIBLE);
                }
                else{
                    _warning.setVisibility(View.GONE);
                    _warningImage.setVisibility(View.INVISIBLE);
                }
            }
        });

        _weeklyReduce.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
                switch (seekParams.thumbPosition){
                    case 0:
                        mWeeklyReduce = 300;
                        break;
                    case 1:
                        mWeeklyReduce = 600;
                        break;
                    case 2:
                        mWeeklyReduce = 1000;
                        break;
                    case 3:
                        mWeeklyReduce = 1500;
                        break;
                }
                ImageView _warningImageWeekly = findViewById(R.id.imgWarningWeekly);
                TextView _warningTextWeekly = findViewById(R.id.txtWarningWeekly);

                if(mWeeklyReduce / 1000f > PersonalData.getInstance().getWeight() * 0.02f){
                    _warningImageWeekly.setVisibility(View.VISIBLE);
                    _warningTextWeekly.setVisibility(View.VISIBLE);
                }
                else{
                    _warningImageWeekly.setVisibility(View.GONE);
                    _warningTextWeekly.setVisibility(View.GONE);
                }
                //Toast.makeText(getBaseContext(), String.valueOf(mWeeklyReduce), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {

            }
        });

        findViewById(R.id.imgNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    PersonalData.getInstance().setGoal_weight(mGoalWeight);
                    PersonalData.getInstance().setWeekly_reduce(mWeeklyReduce);
                    Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
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

        try{
            mGoalWeight = Float.parseFloat(_weight.getText().toString());
        }
        catch (Exception e){
            mGoalWeight = 0.0f;
        }

        if(mGoalWeight <= 1.0f){
            _weight.setError("εισάγετε Βάρος");
            validate = false;
        }
        else{
            _weight.setError(null);
        }
        return validate;
    }
}