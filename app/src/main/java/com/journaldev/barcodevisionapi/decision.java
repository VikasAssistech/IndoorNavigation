package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
class Beacon{
    ArrayList<String> tags;
    Integer intValue;
    String beaconName;
    double distance;
    int count;
    Beacon(String tag, Integer intValue, String beaconName){
            this.intValue = intValue;
            this.beaconName=beaconName;
            this.tags = new ArrayList<String>();
            this.tags.add(tag);
            this.distance=0;
            this.count=0;
    }
}

public class decision extends AppCompatActivity {
    Button navi;
    String allnodesurl = "http://10.17.50.43/wayfinding/nodes.php?building=";
    RequestQueue requestQueue;
    static List<String> categories;
    public TextToSpeech textToSpeech;
    static HashMap<String,Beacon> hashBeacon;
    static HashMap<String,Integer> hashTag;
    static List<Integer> cat;
    String val[];
    private static String data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desicion);
        hashBeacon = new HashMap<String,Beacon>();
        hashTag = new HashMap<String,Integer>();
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        data=getIntent().getStringExtra("data");
        val=data.split(",");
        allnodesurl=allnodesurl+val[1]+"&floor="+val[0];
        categories=new ArrayList<String>();
        cat=new ArrayList<Integer>();
//        initViews();
        requestQueue = Volley.newRequestQueue(decision.this);

        navi=findViewById(R.id.Navigation);
        navi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak("Navigation", TextToSpeech.QUEUE_FLUSH,null);

                categories.clear();
                cat.clear();
                final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                        (Request.Method.GET, allnodesurl, null, new com.android.volley.Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject o1 = response.getJSONObject(i);
                                        if(!hashBeacon.containsKey(o1.getString("name"))){
                                            hashBeacon.put(o1.getString("name"),new Beacon(o1.getString("Tags"),o1.getInt("value"),o1.getString("name")));
                                        }else{
                                            Beacon beacon=hashBeacon.get(o1.getString("name"));
                                            beacon.tags.add(o1.getString("Tags"));
                                        }
                                        hashTag.put(o1.getString("Tags"),o1.getInt("value"));
                                        categories.add(o1.getString("Tags").replaceAll("_"," "));
                                        cat.add(o1.getInt("value"));
                                    }
                                    startActivity(new Intent(decision.this, staticphase.class).putStringArrayListExtra("goku",(ArrayList<String>) categories));
                                    finish();
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
        });
    }
    private void initViews()
    {

    }
    public static String getData()
    {
        return data;
    }
    public static List<String> getCategory()
    {
        return categories;
    }
    public static List<Integer> getValue()
    {
        return cat;
    }

}