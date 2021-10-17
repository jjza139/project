package com.example.testapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.example.testapp.center.Money;


public class addmoney extends Fragment {
    public TextView money;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addmoney, container, false);
        money = (TextView) v.findViewById(R.id.money);
        money.setText(Integer.toString(Money)+".00 THB");
        return v;
    }
}