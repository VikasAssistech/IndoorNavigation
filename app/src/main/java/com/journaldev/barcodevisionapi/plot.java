package com.journaldev.barcodevisionapi;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.util.List;

public class plot extends AppCompatActivity implements SensorEventListener {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    public BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    public Button startexplore, stopexplore, prev, next;
    public UFOBeaconManager ufoBeaconManager;
    Bitmap myBitmap;
    Canvas tempCanvas;
    Paint myPaint1, myPaint2, myPaint3;
    List<String> categories;
    String value = "";
    String nextway = "";
    String prevway = "";
    String val[];
    String nextie[];
    String previe[];
    String vale[];
    public double d1, d11;
    public double d2, d12;
    public double d3, d13;
    public double d4, d14;
    public double d5, d15;
    public double d6, d16;
    public double a, b;
    SensorManager sensorManager;
    TextView display, textexplore;
    float[] mGravity;
    float[] mGeomagnetic;
    double positions[][] = {{7.3, 16.93}, {19.5, 16.93}, {13.4, 23.5}, {9.32, 9.7}, {18.26, 9.7}};
    double distances[] = new double[5];
    int c1 = 0;
    int c2 = 0;
    int c3 = 0;
    int c4 = 0;
    int c5 = 0;
    int c6 = 0;
    int p1 = 0;
    ImageView map;
    TextView myTextView, inspect;
    public final String beacon1 = "55:46:4F:D2:6A:CF";
    public final String beacon2 = "55:46:4F:D2:6A:DD";
    public final String beacon3 = "55:46:4F:11:87:50";
    public final String beacon4 = "55:46:4F:11:88:22";
    public final String beacon5 = "55:46:4F:11:87:13";
    public final String beacon6 = "55:46:4F:11:88:59";
    public final String beacon7 = "55:46:4F:11:88:1B";
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            String[] str = string.split(",");
            if (str[0].equals(beacon1)) {
                d1 = Double.parseDouble(str[1]);
                if (d1 != 0) {
                    c1++;
                    d11 = d11 + d1;
                }
            } else if (str[0].equals(beacon2)) {
                d2 = Double.parseDouble(str[1]);
                if (d2 != 0) {
                    c2++;
                    d12 = d12 + d2;
                }
            } else if (str[0].equals(beacon3)) {
                d3 = Double.parseDouble(str[1]);
                if (d3 != 0) {
                    c3++;
                    d13 = d13 + d3;
                }
            }
            if (str[0].equals(beacon4)) {
                d4 = Double.parseDouble(str[1]);
                if (d4 != 0) {
                    c4++;
                    d14 = d14 + d4;
                }
            } else if (str[0].equals(beacon5)) {
                d5 = Double.parseDouble(str[1]);
                if (d5 != 0) {
                    c5++;
                    d15 = d15 + d5;
                }
            }
            if (c1 > 9 && c2 > 9 && c3 > 9 && c4 > 9 && c5 > 9) {
                distances[0] = d11 / c1;
                distances[1] = d12 / c2;
                distances[2] = d13 / c3;
                distances[3] = d14 / c4;
                distances[4] = d15 / c5;
                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
                LeastSquaresOptimizer.Optimum optimum = solver.solve();
                double[] centroid = optimum.getPoint().toArray();
                a = centroid[0];
                b = centroid[1];
                d11 = 0;
                d12 = 0;
                d13 = 0;
                c1 = 0;
                c2 = 0;
                c3 = 0;
                d14 = 0;
                d15 = 0;
                d16 = 0;
                c4 = 0;
                c5 = 0;
                c6 = 0;
                plott((float) centroid[0], (float) centroid[1]);
                explor();
            }
        }
    };

    private void explor() {
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magnometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(plot.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(plot.this, magnometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey1");
            String[] str = string.split(",");
            if (str[0].equals(beacon1)) {
                d1 = Double.parseDouble(str[1]);
                if (d1 != 0) {
                    c1++;
                    d11 = d11 + d1;
                }
            } else if (str[0].equals(beacon2)) {
                d2 = Double.parseDouble(str[1]);
                if (d2 != 0) {
                    c2++;
                    d12 = d12 + d2;
                }
            } else if (str[0].equals(beacon3)) {
                d3 = Double.parseDouble(str[1]);
                if (d3 != 0) {
                    c3++;
                    d13 = d13 + d3;
                }
            }
            if (str[0].equals(beacon4)) {
                d4 = Double.parseDouble(str[1]);
                if (d4 != 0) {
                    c4++;
                    d14 = d14 + d4;
                }
            } else if (str[0].equals(beacon5)) {
                d5 = Double.parseDouble(str[1]);
                if (d5 != 0) {
                    c5++;
                    d15 = d15 + d5;
                }
            }
            if (c1 > 4 && c2 > 4 && c3 > 4 && c4 > 4 && c5 > 4) {
                distances[0] = d11 / c1;
                distances[1] = d12 / c2;
                distances[2] = d13 / c3;
                distances[3] = d14 / c4;
                distances[4] = d15 / c5;
                double min = distances[0];
                int index = 0;
                for (int a = 0; a < distances.length; a++) {
                    if (min > distances[a]) {
                        min = distances[a];
                        index = a;
                    }
                }
                if (min < 2) {
                    myTextView = findViewById(R.id.display);
                    myTextView.setText("Beacon " + (index + 1));
                    alert();
                }
                NonLinearLeastSquaresSolver solver = new NonLinearLeastSquaresSolver(new TrilaterationFunction(positions, distances), new LevenbergMarquardtOptimizer());
                LeastSquaresOptimizer.Optimum optimum = solver.solve();
                double[] centroid = optimum.getPoint().toArray();
                d11 = 0;
                d12 = 0;
                d13 = 0;
                c1 = 0;
                c2 = 0;
                c3 = 0;
                d14 = 0;
                d15 = 0;
                d16 = 0;
                c4 = 0;
                c5 = 0;
                c6 = 0;
                plott((float) centroid[0], (float) centroid[1]);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plotter);
        map = findViewById(R.id.map);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        textexplore = findViewById(R.id.textexplore);
        startexplore = findViewById(R.id.startexplore);
        stopexplore = findViewById(R.id.stopexplore);
        inspect = findViewById(R.id.inspect);
        prev = findViewById(R.id.prev);
        next = findViewById(R.id.next);
        display = findViewById(R.id.display);
        inspect.setVisibility(View.VISIBLE);
        map.setVisibility(View.INVISIBLE);
        categories = getIntent().getStringArrayListExtra("titu");
        value = categories.get(0);
        prevway = categories.get(1);
        nextway = categories.get(2);
        value = value.substring(0, value.length() - 1);
        prevway = prevway.substring(0, prevway.length() - 1);
        nextway = nextway.substring(0, nextway.length() - 1);
        val = value.split(",");
        previe = prevway.split(",");
        nextie = nextway.split(",");
        inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inspect.setVisibility(View.INVISIBLE);
                map.setVisibility(View.VISIBLE);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.setVisibility(View.INVISIBLE);
                inspect.setVisibility(View.VISIBLE);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p1 > -1 && p1 < nextie.length)
                    display.setText(previe[p1--]);
                if (p1 < 0)
                    p1 = 0;
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p1 < nextie.length && p1 > -1)
                    display.setText(nextie[p1++]);
                if (p1 >= (nextie.length))
                    p1 = nextie.length - 1;
            }
        });
        startexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //textexplore.setText("Arpit is a good boy. But what to do tell me. Can't actually do anything but still i will try to lessen my goodness. Arpit is a good boy. But what to do tell me. Can't actually do anything but still i will try to lessen my goodness. Arpit is a good boy. But what to do tell me. Can't actually do anything but still i will try to lessen my goodness");
            }
        });
        stopexplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textexplore.setText("");
            }
        });
        description();
        checkLocationPermission();
        ufoBeaconManager = new UFOBeaconManager(plot.this);
        if (bluetoothAdapter.isEnabled()) {
            Toast.makeText(plot.this, "Bluetooth On", Toast.LENGTH_SHORT).show();
        } else if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(plot.this, "Bluetooth Off", Toast.LENGTH_SHORT).show();
        }
        plott((float) 5.0, (float) 5.0);
    }

    public void alert() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(plot.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(plot.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(plot.this)
                        .setTitle("hahsk")
                        .setMessage("asdas")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(plot.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(plot.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(plot.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {
                }
                return;
            }
        }
    }

    public void enableBlu() {
        bluetoothAdapter.enable();
        Toast.makeText(plot.this, "Bluetooth ON", Toast.LENGTH_SHORT).show();
    }

    public void disableBlu() {
        bluetoothAdapter.disable();
        Toast.makeText(plot.this, "Bluetooth OFF", Toast.LENGTH_SHORT).show();
    }

    public void enableScan() {
        ufoBeaconManager.startScan(new OnScanSuccessListener() {
            @Override
            public void onSuccess(final UFODevice ufoDevice) {
                runOnUiThread(new Runnable() {
                    Message msg = handler.obtainMessage();
                    Bundle bundle = new Bundle();

                    @Override
                    public void run() {
                        BluetoothDevice bt = ufoDevice.getBtdevice();
                        String store1 = bt.getAddress();
                        double store3 = ufoDevice.getDistance();
                        bundle.putString("myKey", store1 + "," + store3);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    public void enableScan1() {
        ufoBeaconManager.startScan(new OnScanSuccessListener() {
            @Override
            public void onSuccess(final UFODevice ufoDevice) {
                runOnUiThread(new Runnable() {
                    Message msg = handler1.obtainMessage();
                    Bundle bundle = new Bundle();

                    @Override
                    public void run() {
                        BluetoothDevice bt = ufoDevice.getBtdevice();
                        String store1 = bt.getAddress();
                        double store3 = ufoDevice.getDistance();
                        bundle.putString("myKey1", store1 + "," + store3);
                        msg.setData(bundle);
                        handler1.sendMessage(msg);
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(int i, String s) {
            }
        });
    }

    public void disableScan() {
        ufoBeaconManager.stopScan(new OnSuccessListener() {
            @Override
            public void onSuccess(boolean b) {
                if (b) {
                }
                //Toast.makeText(MainActivity.this, "Scan Stopped!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(plot.this, "false", Toast.LENGTH_SHORT).show();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(plot.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void plott(float x, float y) {
        int i, f, n, ff;
        int row1, row2;
        int col1, col2;
        float r1 = 5f;
        x = (float) (x * 13.0434);
        y = (float) (y * 15.306);
        myBitmap = Bitmap.createBitmap(360, 360, Bitmap.Config.ARGB_8888);
        myPaint1 = new Paint();
        myPaint2 = new Paint();
        myPaint3 = new Paint();
        myPaint1.setStyle(Paint.Style.FILL);
        myPaint1.setColor(Color.GREEN);
        myPaint2.setStyle(Paint.Style.FILL);
        myPaint2.setColor(Color.BLUE);
        myPaint3.setStyle(Paint.Style.FILL);
        myPaint3.setColor(Color.RED);
        tempCanvas = new Canvas(myBitmap);
        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
        for (i = 0; i < val.length - 1; i++) {
            f = Integer.parseInt(val[i]);
            n = Integer.parseInt(val[i + 1]);
            row1 = f / 100;
            row2 = n / 100;
            if (row1 == row2) {
                col1 = f % 100;
                col2 = n % 100;
                tempCanvas.drawRect((float) (col1 * 6), (float) ((row1 + 1) * 6), (float) (col2 * 6), (float) ((row1 + 2) * 6), myPaint1);
            } else {
                col1 = f % 100;
                tempCanvas.drawRect((float) (col1 * 6), (float) ((row2 + 1) * 6), (float) ((col1 + 1) * 6), (float) ((row1 + 2) * 6), myPaint1);
            }
        }
        for (i = 0; i < val.length; i++) {
            f = Integer.parseInt(val[i]);
            row1 = f / 100;
            col1 = f % 100;
            if (i == 0)
                tempCanvas.drawRect((float) (col1 * 6), (float) ((row1 + 1) * 6), (float) ((col1 + 1) * 6), (float) ((row1 + 2) * 6), myPaint3);
            else
                tempCanvas.drawRect((float) (col1 * 6), (float) ((row1 + 1) * 6), (float) ((col1 + 1) * 6), (float) ((row1 + 2) * 6), myPaint2);
        }
//        String data=decision.getData();
//        vale=data.split(",");
//        map.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));
//        map.setBackgroundResource(getResources().getIdentifier(vale[1],"drawable",this.getPackageName()));

        for (ff = 1; ff < 28; ff++) {
            tempCanvas.drawRect((float) (13.0434 * ff), (float) 0.0, (float) (13.0434 * ff + 1), (float) 360, myPaint2);
        }
        for (ff = 1; ff < 24; ff++) {
            tempCanvas.drawRect((float) 0.0, (float) (15.306 * ff), (float) 360, (float) (15.306 * ff + 1), myPaint2);
        }
        for (ff = 0; ff < positions.length; ff++) {
            tempCanvas.drawCircle((float) (positions[ff][0] * 13.0434), (float) (positions[ff][1] * 15.306), r1, myPaint3);
        }
        tempCanvas.drawCircle(x, y, r1, myPaint1);
        map.setImageDrawable(new BitmapDrawable(getResources(), myBitmap));
        map.setBackground(getResources().getDrawable(R.drawable.fullsit));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String str = "";
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
                double data = orientationData[0];
                data = Math.round(Math.toDegrees(data));
                data = data - 339; // change here
                if (data < 0) {
                    data += 360;
                }
                display.setText(data + "");
                for (int i = 0; i < 5; i++) {
                    float angle = (float) Math.toDegrees(Math.atan2(positions[i][1] - b, positions[i][0] - a));
                    if (angle < 0) {
                        angle = angle + 360;
                    }
                    if ((angle > data - 30 && angle < data + 30) || (angle > data + 360 - 30 && angle < data + 360 + 30)) {
                        str = str + "Beacon" + " " + (i + 1) + ", ";
                    }
                }
                textexplore.setText(str);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(plot.this);
    }

    private void description() {
        String str = "";
        for (int tt = 0; tt < nextie.length; tt++)
            str = str + nextie[tt] + "\n";
        inspect.setText(str);
    }
}
