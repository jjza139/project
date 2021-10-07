package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // private View decorView;
    private Button signin;
    private  TextView register,forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aclog);
        signin      = (Button) findViewById(R.id.btn_signin);
        register    = (TextView) findViewById(R.id.btn_register);
        forgot      = (TextView) findViewById(R.id.btn_forgot);
        signin.setOnClickListener(this);
        register.setOnClickListener(this);
        forgot.setOnClickListener(this);

    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                //go to register
                startActivity(new Intent(this,registeruser.class));
                break;
            case R.id.btn_signin:
                //go to app
                startActivity(new Intent(this ,center.class));
                break;
            case R.id.btn_forgot:
               //go to forgot
                startActivity(new Intent(this ,center.class));
                break;
        }
    }
}