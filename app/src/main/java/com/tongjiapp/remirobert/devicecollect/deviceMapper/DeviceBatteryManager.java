package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import bolts.Task;
import bolts.TaskCompletionSource;
import io.realm.Realm;

/**
 * Created by remirobert on 01/07/16.
 */
public class DeviceBatteryManager {

    private Context mContext;
    private BroadcastReceiver mBroadcastReceiver;
    private Realm mRealm = Realm.getDefaultInstance();

    public Task<Void> taskBatteryCapacity(String idRecord) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(mContext);
        } catch (Exception e) {
            taskCompletionSource.setError(e);
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

            Record record = mRealm.where(Record.class).equalTo("id", idRecord).findFirst();
            mRealm.beginTransaction();
            record.setBatteryCapacity(batteryCapacity);
            mRealm.commitTransaction();

            taskCompletionSource.setResult(null);
        } catch (Exception e) {
            taskCompletionSource.setError(e);
        }
        return taskCompletionSource.getTask();
    }

    public Task<Void> taskPercentBattery(final String idRecord) {
        final TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                int rawlevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                mContext.unregisterReceiver(mBroadcastReceiver);

                Record record = mRealm.where(Record.class).equalTo("id", idRecord).findFirst();
                mRealm.beginTransaction();
                record.setBatteryPercent(level);
                mRealm.commitTransaction();

                taskCompletionSource.setResult(null);
            }
        };
        mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return taskCompletionSource.getTask();
    }

    public DeviceBatteryManager(Context context) {
        mContext = context;
    }
}
