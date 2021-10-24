package com.example.testapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.testapp.center.Money;
import static com.example.testapp.center.Username;

public class qr extends Fragment {
    public TextView user_name ,money;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_qr, container, false);
        money       =(TextView)v.findViewById(R.id.money);
        user_name   = (TextView) v.findViewById(R.id.User_Name);
        money.setText(Double.toString(Money)+" THB");
        user_name.setText(Username);
        return v;
    }

}