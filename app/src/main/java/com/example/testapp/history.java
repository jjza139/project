package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import static com.example.testapp.center.Username;
import static com.example.testapp.center.Money;

public class history extends Fragment {
    public TextView  money ,username1,username2,username3,username4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        money = (TextView) v.findViewById(R.id.money);
        username1 =(TextView) v.findViewById(R.id.text_user_name1);
        username2 =(TextView) v.findViewById(R.id.text_user_name2);
        username3 =(TextView) v.findViewById(R.id.text_user_name3);
        username4 =(TextView) v.findViewById(R.id.text_user_name4);
        username1.setText(Username);
        username2.setText(Username);
        username3.setText(Username);
        username4.setText(Username);
        money.setText(Double.toString(Money)+" THB");
        return v;
    }


}