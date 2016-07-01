package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by remirobert on 01/07/16.
 */
public class LocalisationManager {

    static final String TAG = "LocalisationManager";
    private LocationClient mLocationClient = null;
    private Context mContext;

    public Task<BDLocation> task() {
        final TaskCompletionSource<BDLocation> task = new TaskCompletionSource<>();

        Log.v(TAG, "task running");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                task.setResult(bdLocation);
            }
        });
        mLocationClient.start();
        return task.getTask();
    }

    public LocalisationManager(Context context) {
        mContext = context;
        mLocationClient = new LocationClient(mContext);
    }
}
