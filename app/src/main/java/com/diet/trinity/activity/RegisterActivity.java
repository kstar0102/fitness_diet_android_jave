package com.diet.trinity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.diet.trinity.R;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diet.trinity.Utility.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText usernameE, useremailE, userpassE, userconfirmE;
    String result, useremail, userpass, userconfirm, username;
    ImageButton register_btn;
    ProgressDialog mProgressDialog;
    ImageView already_account, goLogin, img_back;

    String url, keytoken;
    RequestQueue queue;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_btn = findViewById(R.id.RegisterButton);
        useremailE = findViewById(R.id.EmailAddress);
        userpassE = findViewById(R.id.Password);
        already_account = findViewById(R.id.alreadyaccount);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupProcessing();
            }

        });

        goLogin = findViewById(R.id.goLogin);
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        img_back = findViewById(R.id.imgBack);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signupProcessing() {
        userpass = userpassE.getText().toString();
        useremail = useremailE.getText().toString();

        if (TextUtils.isEmpty(userpass)) {
            Toast.makeText(RegisterActivity.this, "User password Field is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(useremail)) {
            Toast.makeText(RegisterActivity.this, "User Email Field is Empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            if (!(useremail.matches(emailPattern)))
            {
                Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
            }
            else{
                if (!(TextUtils.isEmpty(userpass)))
                {
                    RegisterUser(userpass, useremail);
                }
            }
        }
    }

    private void RegisterUser(final String userpassT,  final String useremailT) {
        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setTitle("SignUp...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();

        userpassE.setEnabled(false);
        useremailE.setEnabled(false);

        url = Common.getInstance().getRegisterUrl();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        mProgressDialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getString("success");
                            if (result.equals("true")){
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                            } else {
                            }
                            already_account.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                        already_account.setVisibility(View.VISIBLE);
                    }
                }){

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", useremailT);
                params.put("password", userpassT);

                params.put("c_password", userpassT);
                params.put("gender", "0");
                params.put("birthday", "1999-09-09");
                params.put("type", "0");

                return params;
            }
        };
        queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(postRequest);
    }
}