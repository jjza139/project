package com.example.testapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class center extends AppCompatActivity {
    public static String Username ,Email;
    // private View decorView;
    private FirebaseUser uAuth;
    private DatabaseReference reference;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu);
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();


       // greeting = (TextView) findViewById(R.id.nameUser);
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home,
                    new home()).commit();
        }

        reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.name;
                    Email = userprofile.email;
                    //   greeting.setText(Username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(center.this,"Failed",Toast.LENGTH_LONG).show();
            }
        });

    }

    protected void onStart(){
        super.onStart();

    }

    public static String getName(){
        return Username;
    }
    public static String getEmail(){
        return Email;
    }

    private void go2main(){
        startActivity(new Intent(this,MainActivity.class));
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.home:
                            selectedFragment = new home();
                            break;
                        case R.id.addmoney:
                            selectedFragment = new addmoney();
                            break;
                        case R.id.qr:
                            selectedFragment = new qr();
                            break;
                        case R.id.history:
                            selectedFragment = new history();
                            break;
                        case R.id.user:
//                            FirebaseAuth.getInstance().signOut();
//                            go2main();
                            selectedFragment = new user();
                            break;
                        default:
                            selectedFragment = new home();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.home,
                            selectedFragment).addToBackStack(null).commit();
                    return true;
                }
            };
}