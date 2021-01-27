package com.example.owner.combined;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class MyService extends Service {
    BroadcastReceiver mReceiver;
    private void showSuccess () {
        Toast.makeText(this, "broadcast received", Toast.LENGTH_SHORT).show();
    }
    public class MyReceiver extends BroadcastReceiver {
        private static final String TAG = "Message Received";
        DatabaseHelper db;


        SQLiteDatabase mydb;


    public MyReceiver(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[]) pudsBundle.get("pdus");
            SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
            DatabaseHelper controllerdb = new DatabaseHelper(context);
            db = new DatabaseHelper(context);

            Cursor res = db.getAllData();
            if (res.getCount() != 0) {
                mydb = controllerdb.getReadableDatabase();
                Cursor cursor = mydb.rawQuery("SELECT * FROM  registeruser", null);

                cursor.moveToFirst();
                Intent smsIntent = new Intent(context, LoginActivity.class);
                smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                smsIntent.putExtra("MessageNumber", messages.getOriginatingAddress());
                smsIntent.putExtra("Message", messages.getMessageBody());
                SmsManager sms = SmsManager.getDefault(); // using android SmsManage

            }


        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
          final IntentFilter it = new IntentFilter();
        it.addAction("android.provider.Telephony.SMS_RECEIVED");
        mReceiver = new MyReceiver();
        registerReceiver(mReceiver, it);
    }
@Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}