package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class staticphase extends AppCompatActivity{
    String shortpathurl ="http://10.17.50.43/wayfinding/RunTest.php?source=";
    String shortpath="";
    List<String> categories;
    List<String> categories1;
    Button btn;
    Spinner spinner1;
    Spinner spinner2;
    String s1="";
    String s2="";
    RequestQueue requestQueue;
    String val[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.staticphase);
        spinner1=findViewById(R.id.spinner1);
        spinner1.setPrompt("Enter Source");
        spinner2=findViewById(R.id.spinner2);
        spinner2.setPrompt("Enter Destination");
        categories=getIntent().getStringArrayListExtra("goku");
        categories1=new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter1=new ArrayAdapter<String>(staticphase.this,android.R.layout.simple_spinner_item,categories);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s1=categories.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayAdapter<String> arrayAdapter2=new ArrayAdapter<String>(staticphase.this,android.R.layout.simple_spinner_item,categories);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(arrayAdapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s2=categories.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        initViews();
    }
    private void initViews() {
        btn=findViewById(R.id.submit_query);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data=decision.getData();
                val=data.split(",");
                shortpath=shortpathurl+s1+"&destination="+s2+"&building="+val[1]+"&floor="+val[0];
                queryval();
            }
        });
    }
    private void queryval() {
        categories1.clear();
        requestQueue = Volley.newRequestQueue(staticphase.this);
        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, shortpath, null, new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String v="";
                        String p="";
                        String n="";
                        try{
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject o1 = response.getJSONObject(i);
                                v=v+o1.getString("value")+",";
                                p=p+o1.getString("prevway")+",";
                                n=n+o1.getString("nextway")+",";
                            }
                            categories1.add(v);
                            categories1.add(p);
                            categories1.add(n);
                            startActivity(new Intent(staticphase.this, plot.class).putStringArrayListExtra("titu", (ArrayList<String>) categories1));
                        }
                        catch (JSONException e){}
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}

