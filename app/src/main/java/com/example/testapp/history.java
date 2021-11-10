package com.example.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

import static com.example.testapp.center.Money;

public class history extends Fragment {
    public TextView  money ,username1,username2,username3,username4;
    RecyclerView recyclerView;
    DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    String UserID;
    MyAdapter myAdapter;
    ArrayList<payinfo> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        UserID = firebaseAuth.getUid();
        final FirebaseUser users = firebaseAuth.getCurrentUser();
        money = (TextView) v.findViewById(R.id.money);
        money.setText(Double.toString(Money)+" THB");

        recyclerView = v.findViewById(R.id.view_history);
        database = FirebaseDatabase.getInstance().getReference("pay/"+UserID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ArrayList<>();

        myAdapter = new MyAdapter(getActivity(),list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    payinfo user = dataSnapshot.getValue(payinfo.class);
                    list.add(user);

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


}