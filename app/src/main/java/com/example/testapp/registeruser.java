package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class registeruser extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText editname,editemail,editphone,editpassword;
    private TextView singin,registerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeruser);
        singin = (TextView) findViewById(R.id.btn_signin);
        singin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        registerUser = (Button) findViewById(R.id.btn_create);
        registerUser.setOnClickListener(this);
        editname = (EditText) findViewById(R.id.editName);
        editemail = (EditText) findViewById(R.id.editEmailAddress);
        editphone = (EditText) findViewById(R.id.editPhone);
        editpassword = (EditText) findViewById(R.id.editName);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_create:
                  registeruser();
                break;
            case R.id.btn_signin:
                    startActivity(new Intent(this,MainActivity.class));
        }
    }
    private void registeruser(){
        String Name =editname.getText().toString().trim();
        String password =editpassword.getText().toString().trim();
        String email =editemail.getText().toString().trim();
        String phone =editphone.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Userinfo user = new Userinfo(Name,email,phone);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(registeruser.this,"Success",Toast.LENGTH_LONG).show();

                                    }
                                    else
                                        Toast.makeText(registeruser.this,"Fail to register2",Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            Toast.makeText(registeruser.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();

                        }

                    }
                });



    }

}