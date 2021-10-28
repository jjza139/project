package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class car extends Fragment {
    private FirebaseUser uAuth;
    private DatabaseReference Ref_plate,Ref_plate_test;
    private String UserId;
    private EditText editplate;
    private Button  btn_create;
    long count_id=0;
    ImageView b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.fragment_car, container, false);
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        editplate = v.findViewById(R.id.editplate);
        btn_create = v.findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a();
            }
        });

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

    private void a(){

        Ref_plate =FirebaseDatabase.getInstance().getReference("Car/"+UserId);
        //[check last]
//        Query last = Ref_plate.orderByChild("plate").limitToLast(1);
//        last.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//              String mess_plate = snapshot.child("plate").getValue().toString();
//                FirebaseDatabase.getInstance().getReference("Users/"+UserId).child("Test").setValue(mess_plate);
//            }
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

        Ref_plate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    count_id= snapshot.getChildrenCount();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        String plate = editplate.getText().toString().trim();
        Ref_plate.child((String.valueOf(count_id+1))).child("plate").setValue(plate);



//        FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/Cars/plate1").setValue(plate)
//        .addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull @NotNull Task<Void> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(getActivity(),"Register Success",Toast.LENGTH_LONG).show();
//
//                }
//                else
//                    Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//
//            }
//        });
    }
   }

