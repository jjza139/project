package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class history extends Fragment {
    public TextView  money ,status;
    RecyclerView recyclerView;
    DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private String UserId;
    private FirebaseUser uAuth;
    MyAdapter myAdapter;
    ArrayList<payinfo> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        status =  v.findViewById(R.id.Text_status);
        firebaseAuth = FirebaseAuth.getInstance();
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        money = (TextView) v.findViewById(R.id.money);
        updateuser();

        recyclerView = v.findViewById(R.id.view_history);
        database = FirebaseDatabase.getInstance().getReference("pay/"+UserId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        myAdapter = new MyAdapter(getActivity(),list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // check history
                    status.setVisibility(View.INVISIBLE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        payinfo user = dataSnapshot.getValue(payinfo.class);
                        list.add(user);
                    }
                }else{
                    list.clear();
                    status.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(),"No",Toast.LENGTH_LONG).show();

                }

                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
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