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

import static java.lang.Long.parseLong;


public class addmoney extends Fragment {
    public TextView money ,result;
    private Button btn_confirm;
    private EditText Edit_Amount;
    private String token_Deeplink ="";
    private String Link="";
    private long Amount;
    private String UserId;
    private DatabaseReference reference;
    private FirebaseUser uAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_addmoney, container, false);
        api Test_api = new api();
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        money = (TextView) v.findViewById(R.id.money);
        updateuser();
        Edit_Amount =v.findViewById(R.id.Edit_Amount);
        Test_api.post_auth();
        btn_confirm=v.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Amount = parseLong(Edit_Amount.getText().toString().trim());
                String token_Deeplink = Test_api.get_token_deeplink();

                Test_api.post_deeplink(token_Deeplink,Amount);
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

    private void updateuser(){
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            private long Money;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Money = userprofile.getMoney();
                    money.setText(Double.toString(Money) + " THB");
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

}