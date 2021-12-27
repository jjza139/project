package com.example.testapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;



public class qr extends Fragment {
    public TextView user_name, money;
    private Button btn_test;
    ImageView qr;
    private DatabaseReference reference;
    private String UserId;
    private FirebaseUser uAuth;
    DatabaseReference mdatabaseReference;
    private long Money;
    Bitmap bitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Car").child(UserId);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_qr, container, false);
        money = (TextView) v.findViewById(R.id.money);
        user_name = (TextView) v.findViewById(R.id.User_Name);
        updateuser();
        qr = v.findViewById(R.id.image_qr);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(UserId, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qr.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return v;
    }

    private void updateuser(){
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            private long Money;
            private String  Username;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.getName();
                    Money = userprofile.getMoney();
                    user_name.setText(Username);
                    money.setText(Long.toString(Money) + " THB");
                    if (Money <=0){
//                        FragmentManager fragmentManager = getFragmentManager();
//                        Fragment selectedFragment = new addmoney();
//                        fragmentManager.beginTransaction().replace(R.id.home,
//                                selectedFragment).addToBackStack(null).commit();
//                        Toast.makeText(getActivity(),"เติมเงิน",Toast.LENGTH_LONG).show();
                    }
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

