package com.example.testapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

public class api {
    private FirebaseUser uAuth;
    private String UserId;
    private String token_Deeplink;
//    private Button sendreq ,btn_refresh,pay,btn_Token;
//    private TextView result,edit_Token,edit_refresh,edit_TokenDeeplink;
    private OkHttpClient client = new OkHttpClient();
    String transactionId ="";
    String a = "" ,b="";
    String deeplinkUrl ="";
    public long paid;
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
    public long get_paid() {
        return paid;
    }

    public String get_token_deeplink(){
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

    public enum transactionType	 {
        BP,
        CCFA,
        CCIPP
    }

    public void post_deeplink(String Bearer,long Amount) {
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
            main.put("sessionValidityPeriod",160);
            main.put("sessionValidUntil","");
            bill.put("paymentAmount",Amount);
            bill.put("accountTo","120191455539804");
            bill.put("ref1","midterm");
            bill.put("ref2","Arinchai");
            bill.put("ref3","Chutikan");
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
                b=response.body().string();
                try {
                    JSONObject json = new JSONObject(b);
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
        check_money();
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
                    JSONObject bill = new JSONObject(data.getString("billPayment"));
                    String account =data.getString("accountFrom");
                     paid =Long.parseLong(bill.getString("paymentAmount"));

                    FirebaseDatabase.getInstance().getReference("Users/"+UserId).child("money").setValue(paid+money);
//                    [check]
//                    FirebaseDatabase.getInstance().getReference("Test/"+UserId).child("id").setValue(transactionId);
//                    FirebaseDatabase.getInstance().getReference("Test/"+UserId).child("token").setValue(token);
//                    FirebaseDatabase.getInstance().getReference("Test/"+UserId).child("paid").setValue(paid);
//                    FirebaseDatabase.getInstance().getReference("Test/"+UserId).child("money").setValue(money);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void check_money(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users/"+UserId+"/money");
        reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                money =Long.parseLong (String.valueOf(task.getResult().getValue()));

                }
            });


    }



}

