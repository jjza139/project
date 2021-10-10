package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class user extends Fragment  {
    public TextView user_name,user_email ;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        user_name = (TextView) v.findViewById(R.id.User_Name);
        user_name.setText(center.getName());
        user_email = (TextView) v.findViewById(R.id.User_email);
        user_email.setText(center.getEmail());
        NavigationView  Nav2 = v.findViewById(R.id.navigationview);
        Nav2.setNavigationItemSelectedListener(navListener);
        // Inflate the layout for this fragment
        return v;
    }





    private NavigationView.OnNavigationItemSelectedListener navListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override

                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.car:
                            selectedFragment = new car();
                            break;
                        case R.id.card:
                            selectedFragment = new card();
                            break;
                        case R.id.qr:
                            selectedFragment = new qr();
                            break;
                        case R.id.faq:
                            selectedFragment = new faq();
                            break;
                        case R.id.setpassword:
                            selectedFragment = new setpassword();
                            break;
                        default:
                            selectedFragment = new user();
                            break;
                    }
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home, selectedFragment).addToBackStack(null).commit();
                    return true;
                }
            };


}