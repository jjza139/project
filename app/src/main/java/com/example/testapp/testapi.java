package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class testapi extends AppCompatActivity {
    private Button sendreq;
    private TextView result;
    private OkHttpClient client = new OkHttpClient();
    String a = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testapi);
//        Intent intent = getIntent();
//        String data = intent.getDataString();
//        String code[] = data.split("=");
        result = findViewById(R.id.result);
        sendreq = findViewById(R.id.sendreq);
        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_auth();
            }
        });

    }

    private void post_auth() {
        //[code request api ]

        // [Code create json and body]
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applicationKey", "l7b26b44c713e746dfa96fca5e635ca566");
            jsonObject.put("applicationSecret", "9f1c5b1884aa4e0285a214ac39ce48ef");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        // [Code create Request and header]
        Request request = new Request.Builder()
                .url("https://api-sandbox.partners.scb/partners/sandbox/v1/oauth/token")
                .post(body)
                .header("Content-Type", "application/json")
                .addHeader("resourceOwnerId", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("requestUId", "{{$guid}}")
                .addHeader("accept-language", "EN")

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                result.setText(response.body().string());
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
                result.setText(response.body().string());

            }
        });

    }

    private void go2scb(String Link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
    }

}