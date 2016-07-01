package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import bolts.Task;
import bolts.TaskCompletionSource;
import io.realm.Realm;

/**
 * Created by remirobert on 01/07/16.
 */
public class LocalisationManager {

    static final String TAG = "LocalisationManager";
    private LocationClient mLocationClient = null;
    private Context mContext;

    private void saveRecord(BDLocation location, String idRecord) {
        Realm realm = Realm.getDefaultInstance();
        Record record = realm.where(Record.class).equalTo("id", idRecord).findFirst();
        realm.beginTransaction();
        record.setLatitude(location.getLatitude());
        record.setLongitude(location.getLongitude());
        realm.commitTransaction();
    }

    public Task<Void>task(final String idRecord) {
        final TaskCompletionSource<Void> task = new TaskCompletionSource<>();

        Log.v(TAG, "task running");
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                saveRecord(bdLocation, idRecord);
                task.setResult(null);
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
