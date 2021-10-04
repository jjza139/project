package com.example.testapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;


public class faq extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    TextView q1,q2,q3;
    TextView a1,a2,a3;
    ImageView b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_faq, container, false);
        q1 = (TextView) v.findViewById(R.id.q1);
        q2 = (TextView) v.findViewById(R.id.q2);
        q3 = (TextView) v.findViewById(R.id.q3);
        a1 = (TextView) v.findViewById(R.id.a1);
        a2 = (TextView) v.findViewById(R.id.a2);
        a3 = (TextView) v.findViewById(R.id.a3);
        q1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a1.getVisibility()==View.GONE) {
                    a1.setVisibility(View.VISIBLE);
                }
                else
                    a1.setVisibility(View.GONE);

            }
        });
        q2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a2.getVisibility()==View.GONE) {
                    a2.setVisibility(View.VISIBLE);
                }
                else
                    a2.setVisibility(View.GONE);

            }
        });
        q3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a3.getVisibility()==View.GONE) {
                    a3.setVisibility(View.VISIBLE);
                }
                else
                    a3.setVisibility(View.GONE);

            }
        });
        b = (ImageView) v.findViewById(R.id.pass_back2);
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