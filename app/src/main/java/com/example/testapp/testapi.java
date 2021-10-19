package com.example.testapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class testapi extends AppCompatActivity {
    private Button sendreq ;
    private TextView result;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testapi);
//        result = findViewById(R.id.result);
//        sendreq.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                req_api();
//            }
//        });

    }

//    private void req_api(){
//        String url = "http://my-json-feed";
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        result.setText("Response: " + response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO: Handle error
//
//                    }
//                });
//
//// Access the RequestQueue through your singleton class.
//       // MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
//    }


}
