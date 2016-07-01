package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by remirobert on 01/07/16.
 */
public class DeviceSignalManager extends PhoneStateListener {

    private TelephonyManager mTelephonyManager;
    private TaskCompletionSource<Integer> mVoidTaskCompletionSource = new TaskCompletionSource<>();


    @Override
    public void onSignalStrengthsChanged(SignalStrength signalStrength) {
        super.onSignalStrengthsChanged(signalStrength);
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_NONE);
        mVoidTaskCompletionSource.setResult(signalStrength.getGsmSignalStrength());
    }

    public Task<Integer> task() {
        mTelephonyManager.listen(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        return mVoidTaskCompletionSource.getTask();
    }

    public DeviceSignalManager(Context context) {
        mTelephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }
}
