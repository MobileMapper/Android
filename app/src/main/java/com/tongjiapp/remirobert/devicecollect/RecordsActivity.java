package com.tongjiapp.remirobert.devicecollect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.tongjiapp.remirobert.devicecollect.deviceMapper.DeviceMapper;
import com.tongjiapp.remirobert.devicecollect.deviceMapper.DeviceMapperListener;
import com.tongjiapp.remirobert.devicecollect.deviceMapper.Record;
import com.tongjiapp.remirobert.devicecollect.deviceMapper.DeviceSignalManager;

import bolts.Continuation;
import bolts.Task;

public class RecordsActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 120;

    private TextView mLocalisationTextView;
    private Button mLocalisationButton;

    public LocationClient mLocationClient = null;


    private void getLocation() {
        Log.v("location", "GO get location");

        DeviceMapper.getRecord(getApplicationContext(), new DeviceMapperListener() {
            @Override
            public void onReceivedRecordDevice(Record record) {
                Log.v("Location", "received location : " + record.getLatitude() + " " + record.getLongitude());
            }
        });
    }

    private void askPersmission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            Log.v("permission", "ask permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Log.v("permission", "already granted");
            getLocation();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        mLocalisationTextView = (TextView) findViewById(R.id.localisation_textview);
        mLocalisationButton = (Button) findViewById(R.id.localisation_button);

        mLocalisationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPersmission();
            }
        });

        DeviceSignalManager deviceSignalManager = new DeviceSignalManager(getApplicationContext());
        deviceSignalManager.task().onSuccess(new Continuation<Integer, Object>() {
            @Override
            public Object then(Task<Integer> task) throws Exception {
                int signalStrenght = task.getResult();
                Log.v("activity", "get signal result = " + signalStrenght);
                return null;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.v("permission", "accepted");
                    getLocation();
                } else {
                    Log.v("permission", "refused");
                }
                return;
            }
        }
    }
}
