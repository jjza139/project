package com.example.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class user extends Fragment  {
    public TextView user_name,user_email ;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private String UserId;
    private FirebaseUser uAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user, container, false);
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        mAuth = FirebaseAuth.getInstance();
        user_name = (TextView) v.findViewById(R.id.User_Name);
        user_email = (TextView) v.findViewById(R.id.User_email);
        updateuser();
        NavigationView  Nav2 = v.findViewById(R.id.navigationview);
        Nav2.setNavigationItemSelectedListener(navListener);
        // Inflate the layout for this fragment
        return v;
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    private void updateuser(){
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            private long Money;
            private String  Username,Email;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.getName();
                    Money = userprofile.getMoney();
                    Email=userprofile.getEmail();
                    user_name.setText(Username);
                    user_email.setText(Email);
                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private final NavigationView.OnNavigationItemSelectedListener navListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override

                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.car:
                            selectedFragment = new car();
                            break;
                        case R.id.card:
                            selectedFragment = new card();
                            break;
                        case R.id.qr:
                            selectedFragment = new qr();
                            break;
                        case R.id.faq:
                            selectedFragment = new faq();
                            break;
                        case R.id.setpassword:
                            selectedFragment = new setpassword();
                            break;
                        case R.id.out:
                            selectedFragment = new user();
                            logout();
                            break;
                        default:
                            selectedFragment = new user();
                            break;
                    }
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home, selectedFragment).addToBackStack(null).commit();
                    return true;
                }
            };


}