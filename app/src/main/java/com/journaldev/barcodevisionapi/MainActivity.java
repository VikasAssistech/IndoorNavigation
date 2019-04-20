package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextToSpeech textToSpeech;
    Button btnScanBarcode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textToSpeech = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                textToSpeech.setLanguage(Locale.ENGLISH);
            }
        });
        setContentView(R.layout.activity_main);
        initViews();
    }
    private void initViews() {
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnScanBarcode.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnScanBarcode:
                textToSpeech.speak("Scan QR Code",TextToSpeech.QUEUE_FLUSH,null);
                startActivity(new Intent(MainActivity.this, ScannedBarcodeActivity.class));
                break;
        }
    }
}