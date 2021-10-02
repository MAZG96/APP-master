package uca.es.test;

import android.app.IntentService;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;


class RebootServiceClass extends IntentService {


    public RebootServiceClass(String name) {
        super(name);
        startForeground(1, new Notification());
    }

    public RebootServiceClass() {
        super("RebootServiceClass");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onHandleIntent(Intent intent) {
        String intentType = intent.getExtras().getString("caller");
        if (intentType == null) return;
        if (intentType.equals("RebootReceiver")) {
            SharedPreferences settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
            Utils.setAlarm(settings.getInt("alarmID", 0), settings.getLong("alarmTime", 0), this);
        }
    }
}