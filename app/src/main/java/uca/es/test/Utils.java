package uca.es.test;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import static android.content.Context.ALARM_SERVICE;

public class Utils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void setAlarm(int i, Long timestamp, Context ctx) {
        AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ctx, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 1, intent, 0);
        System.out.println("Tiempo real: "+timestamp);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent);
    }
}
