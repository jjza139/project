package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class setpassword extends Fragment {
    private TextView password,new_password,confirm_password;
    private Button btn_reset;
    ImageView b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setpassword, container, false);
        password = v.findViewById(R.id.current_password);
        new_password = v.findViewById(R.id.new_password);
        confirm_password = v.findViewById(R.id.confirm_password);
        b = (ImageView) v.findViewById(R.id.pass_back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new user();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home, selectedFragment).addToBackStack(null).commit();
            }
        });

        btn_reset = (Button) v.findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
        return v;
    }


    public void updatePassword() {


        // [START update_password]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentPassword = password.getText().toString().trim();
        String newPassword = new_password.getText().toString().trim();
        String confirmPassword = confirm_password.getText().toString().trim();
        if (currentPassword.isEmpty()|| currentPassword.length() < 6){
            password.setError("Password is requied!");
            password.requestFocus();
            return;
        }
        if (newPassword.isEmpty()|| newPassword.length() < 6){
            new_password.setError("Password is requied!");
            new_password.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()|| confirmPassword.length() < 6){
            confirm_password.setError("Password is requied!");
            confirm_password.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmPassword)){
            new_password.setError("Password does not matches!");
            confirm_password.setError("Password does not matches!");
            new_password.requestFocus();
            return;
        }

        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),"password updated",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
        // [END update_password]
    }
}