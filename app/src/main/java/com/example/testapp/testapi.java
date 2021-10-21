package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class testapi extends AppCompatActivity {
    private FirebaseUser uAuth;
    private String UserId;

    private Button sendreq ,btn_refresh,pay,btn_Token;
    private TextView result,edit_Token,edit_refresh,edit_TokenDeeplink;
    private OkHttpClient client = new OkHttpClient();
    String token ="" ;
    String token_deeplink ="" ;
    String refreshtoken="";
    String a = "";
    String transactionId ="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testapi);

        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        UserId = uAuth.getUid();

        result = findViewById(R.id.result);
        edit_TokenDeeplink=findViewById(R.id.edit_TokenDeeplink);
        edit_Token = findViewById(R.id.edit_Token);
        edit_Token.setText("Token : "+token);
        edit_refresh = findViewById(R.id.edit_refresh);
        edit_refresh.setText("Token Refresh : "+refreshtoken);
        sendreq = findViewById(R.id.sendreq);
        sendreq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_auth();

            }
        });
        btn_refresh = findViewById(R.id.btn_refreshtoken);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_refreshtoken(refreshtoken);

            }
        });

        pay=findViewById(R.id.btn_pay);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_deeplink(token_deeplink);
            }
        });

        btn_Token=findViewById(R.id.btn_Token);
        btn_Token.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_auth();
            }
        });


    }
    private void updata_data(){
        edit_Token.setText("Token : "+token);
        edit_refresh.setText("Token Refresh: "+refreshtoken);
        edit_TokenDeeplink.setText("Token Deeplink:" +token_deeplink);
    }


    private void update_code(){
        Intent intent = getIntent();
        try {
            String code[] = intent.getDataString().split("=");
            if(code[1].length() > 30){
                result.setText("Authcode: "+code[1]);
                post_token(code[1]);
            }else{
                //pay Success
                result.setText(code[1]);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/Transaction");
                reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            try {
                                JSONObject trans = new JSONObject(String.valueOf(task.getResult().getValue()));
                                transactionId = trans.getString("Id");
                                token_deeplink = trans.getString("token_deeplink");
                                get_transaction(transactionId,token_deeplink);

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
                a=response.body().string();
                try {
                    JSONObject json = new JSONObject(a);
                    JSONObject data = new JSONObject(json.getString("data"));
                    token_deeplink= data.getString("accessToken");
                    updata_data();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    result.setText(link.getString("callbackUrl"));
                    go2scb(link.getString("callbackUrl"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // a=response.body().string();


            }
        });

    }

    private void post_token(String authcode) {
        //[code request api ]
        // [Code create json and body]
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applicationKey", "l7b26b44c713e746dfa96fca5e635ca566");
            jsonObject.put("applicationSecret", "9f1c5b1884aa4e0285a214ac39ce48ef");
            jsonObject.put("authCode", authcode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
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
            public void onFailure(@NotNull Call call, @NotNull IOException e) { }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                a=response.body().string();
                try {
                    JSONObject json = new JSONObject(a);
                    JSONObject data = new JSONObject(json.getString("data"));
                    token=data.getString("accessToken");
                    refreshtoken=data.getString("refreshToken");
                    updata_data();
//                    result.setText(link.getString("accessToken"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                result.setText(a);


            }
        });

    }

    private void post_refreshtoken(String Refreshtoken) {
        //[code request api ]
        // [Code create json and body]
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("applicationKey", "l7b26b44c713e746dfa96fca5e635ca566");
            jsonObject.put("applicationSecret", "9f1c5b1884aa4e0285a214ac39ce48ef");
            jsonObject.put("refreshToken", Refreshtoken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url("https://api-sandbox.partners.scb/partners/sandbox/v1/oauth/token/refresh")
                .post(body)
                .header("Content-Type", "application/json")
                .addHeader("resourceOwnerId", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("requestUId", "{{$guid}}")
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
                    JSONObject data = new JSONObject(json.getString("data"));
                    token=data.getString("accessToken");
                    refreshtoken=data.getString("refreshToken");
                    updata_data();
//                    result.setText(link.getString("accessToken"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                result.setText(a);


            }
        });

    }


    private void post_deeplink(String Bearer) {
        //[code request api ]
        // [Code create json and body]
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject main = new JSONObject();
        JSONArray bp =new JSONArray();
        JSONObject bill = new JSONObject();
        JSONObject merchant = new JSONObject();
        JSONObject merchantinfo = new JSONObject();
        int Amount = 100;
        try {
            bp.put("BP");
            main.put("transactionType", "PURCHASE");
            main.put("transactionSubType", bp);
            main.put("sessionValidityPeriod",160);
            main.put("sessionValidUntil","");
            bill.put("paymentAmount",Amount);
            bill.put("accountTo","120191455539804");
            bill.put("ref1","a");
            bill.put("ref2","b");
            bill.put("ref3","c");
            main.put("billPayment",bill);

            merchant.put("callbackUrl","myapp://testapp");
            merchantinfo.put("name","SANDBOX MERCHANT NAME");
            merchant.put("merchantInfo",merchantinfo);
            main.put("merchantMetaData",merchant);

        } catch (JSONException e) {
            e.printStackTrace();
        }

//        edit_refresh.setText(main.toString());
        RequestBody body = RequestBody.create(JSON, main.toString());
        Request request = new Request.Builder()
                .url("https://api-sandbox.partners.scb/partners/sandbox/v3/deeplink/transactions")
                .post(body)
                .header("Content-Type", "application/json")
                .addHeader("authorization","Bearer "+Bearer)
                .addHeader("resourceOwnerId", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("requestUId", "{{$guid}}")
                .addHeader("channel","scbeasy")
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
                    JSONObject data = new JSONObject(json.getString("data"));
                    transactionId= data.getString("transactionId");
                    FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/Transaction/Id").setValue(transactionId);
                    FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/Transaction/token_deeplink").setValue(token_deeplink);
                    FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/Transaction/Amount").setValue(Amount);
                    go2scb(data.getString("deeplinkUrl"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void get_transaction(String transactionId ,String token) {
        //[code request api ]
        Request request = new Request.Builder()
                .url("https://api-sandbox.partners.scb/partners/sandbox/v2/transactions/"+transactionId)
                .header("authorization", "Bearer "+token)
                .addHeader("resourceOwnerId", "l7b26b44c713e746dfa96fca5e635ca566")
                .addHeader("requestUId", "{{$guid}}")
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
                    JSONObject data = new JSONObject(json.getString("data"));
                    String account =data.getString("accountFrom");
                    String paid =data.getString("paidAmount");
                    result.setText("Account : "+account+"\nPaid : "+paid);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    private void go2scb(String Link) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(browserIntent);
    }

}