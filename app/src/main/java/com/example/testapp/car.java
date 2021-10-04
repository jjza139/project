package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class car extends Fragment {
    ImageView b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_car, container, false);
        b = (ImageView) v.findViewById(R.id.pass_back4);
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
