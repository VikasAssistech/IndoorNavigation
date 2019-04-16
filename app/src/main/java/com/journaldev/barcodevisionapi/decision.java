package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class decision extends AppCompatActivity {
    Button navi;
    String allnodesurl = "http://10.17.50.43/wayfinding/nodes.php?building=";
    RequestQueue requestQueue;
    List<String> categories;
    String val[];
    private static String data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desicion);
        data=getIntent().getStringExtra("data");
        val=data.split(",");
        allnodesurl=allnodesurl+val[1]+"&floor="+val[0];
        categories=new ArrayList<String>();
        initViews();
    }
    private void initViews()
    {
        navi=findViewById(R.id.Navigation);
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestQueue = Volley.newRequestQueue(decision.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, allnodesurl, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONArray cordsArray = response.getJSONArray("cords");
                                    for (int i = 0; i < cordsArray.length(); i++) {
                                        JSONObject o1 = cordsArray.getJSONObject(i);
                                        categories.add(o1.getString("Tags"));
                                    }
                                    startActivity(new Intent(decision.this, staticphase.class).putStringArrayListExtra("goku",(ArrayList<String>) categories));
                                } catch (JSONException e) { }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                requestQueue.add(jsonObjectRequest);
            }
        });
    }
    public static String getData()
    {
        return data;
    }
}
