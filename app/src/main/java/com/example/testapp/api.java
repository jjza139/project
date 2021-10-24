package com.example.testapp;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class api {
    private FirebaseUser uAuth;
    private String UserId;
    private String token_Deeplink;
//    private Button sendreq ,btn_refresh,pay,btn_Token;
//    private TextView result,edit_Token,edit_refresh,edit_TokenDeeplink;
    private OkHttpClient client = new OkHttpClient();
    String transactionId ="";
    String a = "";
    String deeplinkUrl ="";
    long amount,money;




    api(){
        uAuth = FirebaseAuth.getInstance().getCurrentUser();
        UserId = uAuth.getUid();

    }

    public long get_money() {
        return money;
    }
    public long get_amount() {
        return amount;
    }

    String get_token_deeplink(){
        return token_Deeplink;
    }

    String get_deeplinkUrl(){
        return deeplinkUrl;
    }

    public void post_auth() {
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
                    token_Deeplink =data.getString("accessToken");
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Id").setValue("");
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Amount").setValue(0.00);
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/token_deeplink").setValue(token_Deeplink);
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Status").setValue("Pending");

//                    post_deeplink(token_Deeplink,Amount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        return token_Deeplink;
    }



    public void post_deeplink(String Bearer,double Amount) {
        //[code request api ]
        // [Code create json and body]
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject main = new JSONObject();
        JSONArray bp =new JSONArray();
        JSONObject bill = new JSONObject();
        JSONObject merchant = new JSONObject();
        JSONObject merchantinfo = new JSONObject();
        try {
            bp.put("BP");
            main.put("transactionType", "PURCHASE");
            main.put("transactionSubType", bp);
            main.put("sessionValidityPeriod",Amount);
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
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Id").setValue(transactionId);
                    FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current/Amount").setValue(Amount);
                    deeplinkUrl =(data.getString("deeplinkUrl"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
//        return deeplinkUrl;

    }


    public void get_transaction(String transactionId ,String token) {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    public void check_money(){
        DatabaseReference Ref_amount = FirebaseDatabase.getInstance().getReference("Transaction/"+UserId+"/Current");
        Ref_amount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                amount = (long) dataSnapshot.child("Amount").getValue();
//                amount= Integer.parseInt(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        DatabaseReference Ref_money = FirebaseDatabase.getInstance().getReference("Users/"+UserId);
        Ref_money.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                money = (long) dataSnapshot.child("money").getValue();
//                amount= Integer.parseInt(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

            long sum =money+amount;
        FirebaseDatabase.getInstance().getReference("Users/"+UserId).child("money").setValue(sum);

    }






}

