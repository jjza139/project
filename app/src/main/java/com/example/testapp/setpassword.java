package com.example.testapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.TextView;


public class setpassword extends Fragment {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    ImageView b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setpassword, container, false);
        b = (ImageView) v.findViewById(R.id.pass_back);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedFragment = new user();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home, selectedFragment).addToBackStack(null).commit();
            }
        });
        return v;
    }
}