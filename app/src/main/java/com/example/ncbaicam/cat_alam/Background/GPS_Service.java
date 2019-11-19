package com.example.ncbaicam.cat_alam.Background;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.ncbaicam.cat_alam.Item.ResponseBody;
import com.example.ncbaicam.cat_alam.Item.UserLocationItem;
import com.example.ncbaicam.cat_alam.remote.RemoteService;
import com.example.ncbaicam.cat_alam.remote.ServiceGenerator;

import java.util.concurrent.TimeUnit;

import retrofit2.Call;

/*
    백그라운드에서 위치 저장을 위한 JobScheduler
 */
public class GPS_Service extends JobService {
    int count = 0;
    boolean isGPSEnable = false;
    boolean isNetWorkEnable = false;
    boolean isGetLocation = false;

    Location location;
    double lat;
    double lng;

    private static final long MIN_DISTANCE_UPDATE = 0;
    private static final long MIN_TIME_UPDATE = 0;

    protected LocationManager locationManager;

    @Override
    public boolean onStartJob(final JobParameters params) {
        Log.d("Service", "onStartJob: Start");
        Handler handler = new Handler();
        getLocation();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                if(isGetLocation){
                    Log.d("Service", "lng" + lng);
                    Log.d("Service", "lat" + lat);
                    SharedPreferences appData = getSharedPreferences("Register", MODE_PRIVATE);
                    String u_pnumber = appData.getString("u_pnumber", "null");
                    UserLocationItem userLocationItem = new UserLocationItem(u_pnumber, lng, lat);
                    RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);
                    Call<ResponseBody> call = remoteService.insertLocation(userLocationItem);
                }
            }
        },3000); //3초 뒤에 Runner객체 실행하도록 함

        Log.d("Service", "onStartJob: Fail");
        return false;

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lng = location.getLongitude();
            lat = location.getLatitude();
            isGetLocation = true;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public void getLocation() {
        locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_UPDATE, MIN_TIME_UPDATE, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_DISTANCE_UPDATE, MIN_TIME_UPDATE, locationListener);
    }

    public void setService(){
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(0, new ComponentName(this, GPS_Service.class))
                .setMinimumLatency(TimeUnit.MINUTES.toMillis(1)) // 시간 바꿔야함
                .setOverrideDeadline(TimeUnit.MINUTES.toMillis(3))
                .build());
    }

}
