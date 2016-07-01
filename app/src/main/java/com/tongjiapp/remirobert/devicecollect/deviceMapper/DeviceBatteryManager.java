package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by remirobert on 01/07/16.
 */
public class DeviceBatteryManager {

    private Context mContext;
    private BroadcastReceiver mBroadcastReceiver;

    public Task<Double> taskBatteryCapacity() {
        TaskCompletionSource<Double> taskCompletionSource = new TaskCompletionSource<>();
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
            taskCompletionSource.setResult(batteryCapacity);
        } catch (Exception e) {
            taskCompletionSource.setError(e);
        }
        return taskCompletionSource.getTask();
    }

    public Task<Integer> taskPercentBattery() {
        final TaskCompletionSource<Integer> taskCompletionSource = new TaskCompletionSource<>();

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
                taskCompletionSource.setResult(level);
            }
        };
        mContext.registerReceiver(mBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return taskCompletionSource.getTask();
    }

    public DeviceBatteryManager(Context context) {
        mContext = context;
    }
}
