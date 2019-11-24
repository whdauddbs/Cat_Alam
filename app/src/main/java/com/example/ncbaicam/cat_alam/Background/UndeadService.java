package com.example.ncbaicam.cat_alam.Background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ncbaicam.cat_alam.LogoIntro;
import com.example.ncbaicam.cat_alam.MainPage;
import com.example.ncbaicam.cat_alam.R;
import com.example.ncbaicam.cat_alam.UserLocation;

import java.util.Calendar;

public class UndeadService extends Service {
    public static Intent serviceIntent = null;
    private  UserLocation userLocation;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceIntent = intent;
        initializeNotification();
        
        // 위치 저장 하기
        Log.d("Service", "onStartCommand: 위치 저장 시작");
        userLocation = new UserLocation(this, intent.getStringExtra("phoneNum"));
        userLocation.setLocation();
        // 거리가 가까워지면 알람울리기, 푸쉬알림

        
        return START_STICKY;
    }

    private void initializeNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText("설정을 보려면 누르세요.");
        style.setBigContentTitle(null);
        style.setSummaryText("서비스 동작중");
        double s = 1;

        builder.setContentText(null);
        builder.setContentTitle(null);
        builder.setOngoing(true);

        builder.setStyle(style);
        builder.setWhen(0);
        builder.setShowWhen(false);

        Intent notificationIntent = new Intent(this, LogoIntro.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent, 0);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(new NotificationChannel("1", "undead_service", NotificationManager.IMPORTANCE_NONE));
        }
        Notification notification = builder.build();
        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        userLocation.stopLocation();
        Log.d("Service", "onStartCommand: 위치 저장 꺼짐");
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0 ,intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        userLocation.stopLocation();
        Log.d("Service", "onStartCommand: 위치 저장 꺼짐");
        final  Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 3);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(this, 0 , intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    
}
