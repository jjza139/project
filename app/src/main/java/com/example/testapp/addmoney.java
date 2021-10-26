package com.example.testapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.testapp.center.Money;


public class addmoney extends Fragment {
    public TextView money ,result;
    private Button btn_confirm;
    private EditText Edit_Amount;
    private String token_Deeplink ="";
    private String Link="";
    private double Amount;
    private String UserId;
    api Test_api = new api();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addmoney, container, false);
        FirebaseUser uAuth = FirebaseAuth.getInstance().getCurrentUser();
        UserId = uAuth.getUid();
        money = (TextView) v.findViewById(R.id.money);
        money.setText(Double.toString(Money)+" THB");
        Edit_Amount =v.findViewById(R.id.Edit_Amount);
        Test_api.post_auth();
        btn_confirm=v.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount = Double.parseDouble(Edit_Amount.getText().toString().trim());
                Test_api.post_deeplink(Test_api.get_token_deeplink(),Amount);
                go2scb(Test_api.get_deeplinkUrl());

//                Money=Test_api.get_money();
//                Amount=Test_api.get_amount();
//                FirebaseDatabase.getInstance().getReference("Users/"+UserId).child("money").setValue(Amount);

            }
        });

        return v;
    }

    private void go2scb(String Link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Define what your app should do if no activity can handle the intent.
            e.getMessage().toString();
        }

    }



}