package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;

import com.baidu.location.BDLocation;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by remirobert on 01/07/16.
 */

public class DeviceMapper {

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
