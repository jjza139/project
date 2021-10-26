package com.example.testapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class center extends AppCompatActivity {
    public static String Username ,Email,Status;
    public static long Money ;
     long Moneya ,Amount;
    // private View decorView;
    private FirebaseUser uAuth;
    private DatabaseReference reference;
    private String UserId;
    api Test_api = new api();
    String transactionId,token_deeplink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu);
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        updateuser();

//        Intent intent = getIntent();
//        String action = intent.getAction();
//        Uri data = intent.getData();

       // greeting = (TextView) findViewById(R.id.nameUser);
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home,
                    new home()).commit();
        }


    }
    private void updateuser(){
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.name;
                    Email = userprofile.email;
                    Money = userprofile.money;
                    //greeting.setText(Username);

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(center.this,"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }


    private void update_code(){
        Intent intent = getIntent();
        try {
            String code[] = intent.getDataString().split("=");
            if(code[1].length() > 30){
                Status=code[1];
//                FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Status").setValue(Status);
            }else{
                Status=code[1];
//                Test_api.check_money();
                FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Status").setValue(Status);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current");
                reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                JSONObject trans = new JSONObject(String.valueOf(task.getResult().getValue()));
                                transactionId = trans.getString("Id");
                                token_deeplink = trans.getString("token_deeplink");
                                Test_api.get_transaction(transactionId,token_deeplink);
                                Amount = Test_api.get_paid();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else{

                        }

                    }
                });
            }

        }catch (Exception e){
            // result.setText(data);

        }

    }

    protected void onStart(){
        super.onStart();
        update_code();

    }

    public static String getName(){
        return Username;
    }
    public static String getEmail(){
        return Email;
    }
    public static double getMoney(){
        return Money;
    }

    public void go2scb(String Link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
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
                            selectedFragment = new user();
                            break;
                        default:
                            updateuser();
                            selectedFragment = new home();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.home,
                            selectedFragment).addToBackStack(null).commit();
                    return true;
                }
            };
}