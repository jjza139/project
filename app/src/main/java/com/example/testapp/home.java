package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class home extends Fragment  {
    public static String Username ,Email;
    public static double Money;
    private DatabaseReference reference;
    private FirebaseUser uAuth;
    private String UserId;
    public TextView greeting;
    private OkHttpClient client = new OkHttpClient();
    String a;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        greeting = (TextView) v.findViewById(R.id.User_Name);


        return v;

    }

    public void onStart() {
        super.onStart();
        updateuser();
    }

    private void updateuser(){
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();
        reference.child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Userinfo userprofile = snapshot.getValue(Userinfo.class);

                if(userprofile != null) {
                    Username = userprofile.getName();
                    Email = userprofile.getEmail();
                    Money = userprofile.getMoney();
                    greeting.setText(Username);

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void get_auth() {
        //[code request api ]
        Request request = new Request.Builder()
                .url("https://api-sandbox.partners.scb/partners/sandbox/v2/oauth/authorize")
                .header("apikey", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("apisecret", "9f1c5b1884aa4e0285a214ac39ce48ef")
                .addHeader("resourceOwnerId", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("requestUId", "{{$guid}}")
                .addHeader("response-channel", "mobile")
                .addHeader("endState", "mobile_app")
                .addHeader("accept-language", "EN")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                a=response.body().string();
                try {
                    JSONObject json = new JSONObject(a);
                    JSONObject link = new JSONObject(json.getString("data"));
                    //result.setText(link.getString("callbackUrl"));
                    go2scb(link.getString("callbackUrl"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // a=response.body().string();


            }
        });

    }

    private void go2scb(String Link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
    }


}

