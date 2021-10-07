package com.example.testapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class home extends Fragment {
    private FirebaseUser uAuth;
    private DatabaseReference reference;
    private String UserId;
    public TextView greeting;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
       reference = FirebaseDatabase.getInstance().getReference("Users");
         UserId = uAuth.getUid();

        greeting = (TextView) v.findViewById(R.id.nameUser);

        reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userpofile = snapshot.getValue(Userinfo.class);

                if(userpofile != null){
                    String Username = userpofile.name;
                    greeting.setText(Username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return v;
    }
}