package com.journaldev.barcodevisionapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Plotter extends AppCompatActivity implements SensorEventListener{
    ImageView map22;
    TextView inspect,display,compass;
    Button expand;
    Button explore,nex,pre;
    List<String> categories;
    String value="";
    String nextway="";
    String prevway="";
    String val[];
    String next[];
    String prev[];
    String vale[];
    SensorManager sensorManager;
    float[] mGravity;
    float[] mGeomagnetic;
    int p1=0;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plotter);
        inspect=findViewById(R.id.inspect);
        display=findViewById(R.id.display);
        map22=findViewById(R.id.map);
        //compass=findViewById(R.id.compass);
        inspect.setVisibility(View.VISIBLE);
        map22.setVisibility(View.INVISIBLE);
        categories=getIntent().getStringArrayListExtra("titu");
        value=categories.get(0);
        prevway=categories.get(1);
        nextway=categories.get(2);
        value=value.substring(0,value.length()-1);
        prevway=prevway.substring(0,prevway.length()-1);
        nextway=nextway.substring(0,nextway.length()-1);
        val=value.split(",");
        prev=prevway.split(",");
        next=nextway.split(",");
        initViews();
        description();
        plot();
    }
    private void initViews()
    {
        //expand = findViewById(R.id.expand);
        //explore=findViewById(R.id.explore);
        nex=findViewById(R.id.next);
        pre=findViewById(R.id.prev);
        inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspect.setVisibility(View.INVISIBLE);
                map22.setVisibility(View.VISIBLE);
            }
        });
        map22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map22.setVisibility(View.INVISIBLE);
                inspect.setVisibility(View.VISIBLE);
            }
        });
//        expand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Plotter.this,  staticphasezoom.class));
//            }
//        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p1>-1 && p1<next.length)
                    display.setText(prev[p1--]);
                if(p1<0)
                    p1=0;
            }
        });
        nex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(p1<next.length && p1>-1)
                    display.setText(next[p1++]);
                if(p1>=(next.length))
                    p1=next.length-1;
            }
        });
//        explore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//                explor();
//            }
//        });
    }
//    protected void onPause() {
//        super.onPause();
//        sensorManager.unregisterListener(this);
//    }
    private void plot()
    {
        int i,f,n;
        int row1,row2;
        int col1,col2;
        Bitmap myBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
        Paint myPaint1 = new Paint();
        Paint myPaint2=new Paint();
        Paint myPaint3=new Paint();
        myPaint1.setStyle(Paint.Style.FILL);
        myPaint2.setStyle(Paint.Style.FILL);
        myPaint3.setStyle(Paint.Style.FILL);
        myPaint1.setColor(Color.GREEN);
        myPaint2.setColor(Color.RED);
        myPaint3.setColor(Color.BLUE);
        Canvas tempCanvas = new Canvas(myBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        for(i=0;i<val.length-1;i++)
        {
            f=Integer.parseInt(val[i]);
            n=Integer.parseInt(val[i+1]);
            row1=f/100;
            row2=n/100;
            if(row1==row2)
            {
                col1=f%100;
                col2=n%100;
                tempCanvas.drawRect(col1*5,(row1+1)*5,col2*5,(row1+2)*5,myPaint1);
            }
            else {
                col1=f%100;
                tempCanvas.drawRect(col1*5,(row2+1)*5,(col1+1)*5,(row1+2)*5,myPaint1);
            }
        }
        for(i=0;i<val.length;i++) {
            f = Integer.parseInt(val[i]);
            row1 = f / 100;
            col1 = f % 100;
            if (i == 0)
                tempCanvas.drawRect(col1 * 5, (row1+1) * 5, (col1+1) * 5, (row1+2) * 5, myPaint2);
            else
                tempCanvas.drawRect(col1 * 5, (row1+1) * 5, (col1 + 1) * 5, (row1+2) * 5, myPaint3);
        }
        String data=decision.getData();
        vale=data.split(",");
        map22.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));
        map22.setBackgroundResource(getResources().getIdentifier(vale[1],"drawable",this.getPackageName()));
    }
    private void description()
    {
        String str="";
        for(int tt=0;tt<next.length;tt++)
            str=str+next[tt]+"\n";
        inspect.setText(str);
    }
//    private void explor() {
//        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        Sensor magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientationData[] = new float[3];
                SensorManager.getOrientation(R, orientationData);
                compass.setText(Math.round(Math.toDegrees(orientationData[0]))+"");
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }
}

