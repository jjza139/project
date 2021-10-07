package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // private View decorView;
    private Button signin;
    private  TextView register,forgot;
    private EditText editemail,editpassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aclog);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        signin      = (Button) findViewById(R.id.btn_signin);
        register    = (TextView) findViewById(R.id.btn_register);
        forgot      = (TextView) findViewById(R.id.btn_forgot);
        editemail = (EditText) findViewById(R.id.emailLogin);
        editpassword = (EditText) findViewById(R.id.passLogin);
        signin.setOnClickListener(this);

        register.setOnClickListener(this);
        forgot.setOnClickListener(this);



    }

    private void validateLogin() {
        String password =editpassword.getText().toString().trim();
        String email =editemail.getText().toString().trim();

        if (email.isEmpty()){
            editemail.setError("email is requied!");
            editemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editemail.setError("Please provide valid email!");
            editemail.requestFocus();
            return;
        }
        if (password.isEmpty()|| password.length() < 6){
            editpassword.setError("password is requied!");
            editpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    sendUsertoCenter();
                    Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    private void sendUsertoCenter() {
        Intent intent = new Intent(this,center.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_register:
                //go to register
                startActivity(new Intent(this,registeruser.class));
                break;
            case R.id.btn_signin:
                //go to app
                validateLogin();
                break;
            case R.id.btn_forgot:
               //go to forgot
                startActivity(new Intent(this ,center.class));
                break;
        }
    }
}