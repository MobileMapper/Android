package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;

import com.baidu.location.BDLocation;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by remirobert on 01/07/16.
 */

public class DeviceMapper {

    private Task<DeviceRecord>taskRecordDevice() {
        TaskCompletionSource<DeviceRecord> taskCompletionSource = new TaskCompletionSource<>();

        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                return null;
            }
        });
        return taskCompletionSource.getTask();
    }

    public static void getRecord(Context context, final DeviceMapperListener listener) {
        LocalisationManager localisationManager = new LocalisationManager(context);
        final DeviceRecord deviceRecord = new DeviceRecord();

        localisationManager.task().onSuccess(new Continuation<BDLocation, Object>() {
            @Override
            public Object then(Task<BDLocation> task) throws Exception {
                BDLocation bdLocation = task.getResult();
                deviceRecord.setLatitude(bdLocation.getLatitude());
                deviceRecord.setLongitude(bdLocation.getLongitude());
                listener.onReceivedRecordDevice(deviceRecord);
                return null;
            }
        });
    }
}
