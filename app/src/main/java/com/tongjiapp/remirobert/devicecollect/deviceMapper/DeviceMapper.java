package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import android.content.Context;
import android.util.Log;

import java.util.UUID;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by remirobert on 01/07/16.
 */

public class DeviceMapper {

    private Task<Record>taskRecordDevice() {
        TaskCompletionSource<Record> taskCompletionSource = new TaskCompletionSource<>();

        Task.callInBackground(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                return null;
            }
        });
        return taskCompletionSource.getTask();
    }

    public static void getRecord(Context context, final DeviceMapperListener listener) {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        LocalisationManager localisationManager = new LocalisationManager(context);
        final DeviceBatteryManager deviceBatteryManager = new DeviceBatteryManager(context);

        final String idRecord = UUID.randomUUID().toString();
        final Record record = new Record(idRecord);


        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(record);
            }
        });

        localisationManager.task(idRecord).continueWithTask(new Continuation<Void, Task<? extends Object>>() {
            @Override
            public Task<? extends Object> then(Task<Void> task) throws Exception {
                return deviceBatteryManager.taskBatteryCapacity();
            }
        }).onSuccess(new Continuation<TContinuationResult, Object>() {
            @Override
            public Object then(Task<TContinuationResult> task) throws Exception {

                return null;
            }
        });

        localisationManager.task(idRecord).onSuccess(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {

                Record recordedDevice = realm.where(Record.class).equalTo("id", idRecord).findFirst();
                Log.v("lcoation result", "recorded device : " + recordedDevice.getLatitude() + " " + recordedDevice.getLongitude());
                return null;
            }
        });
    }
}
