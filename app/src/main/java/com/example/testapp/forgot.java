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

public class forgot extends AppCompatActivity implements View.OnClickListener{
    // private View decorView;
    private Button reset,login;
    private FirebaseAuth mAuth;
    private EditText editemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_got);
        reset = findViewById(R.id.btn_reset);
        reset.setOnClickListener(this);
        login =findViewById(R.id.login);
        login.setOnClickListener(this);
        editemail =findViewById(R.id.EditEmail);
        mAuth = FirebaseAuth.getInstance();

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                //go to register
                resetPassword();
                // startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.login:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }
//

    private void resetPassword(){
        String email = editemail.getText().toString().trim();
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

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(forgot.this, "Check your email to reset password", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(forgot.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });



    }
}