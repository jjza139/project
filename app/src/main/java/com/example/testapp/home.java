package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import okhttp3.OkHttpClient;


public class home extends Fragment  {
    public  String Username ;
    private DatabaseReference reference;
    private FirebaseUser uAuth;
    private String UserId;
    public TextView greeting;
    private OkHttpClient client = new OkHttpClient();
    String a;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        greeting = (TextView) v.findViewById(R.id.User_Name);
        updateuser();

        return v;

    }

    private void updateuser(){
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        UserId = uAuth.getUid();
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.getName();
                    greeting.setText(Username);

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void go2scb(String Link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
    }


}

