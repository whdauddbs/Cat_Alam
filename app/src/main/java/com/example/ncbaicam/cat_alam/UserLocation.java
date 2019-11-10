package com.example.ncbaicam.cat_alam;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.List;

import static android.support.v4.content.ContextCompat.getSystemService;

public class UserLocation implements LocationListener {
    private Context mContext;
    LocationManager locationManager;
    private List<String> listProviders;
    private static long MIN_DISTANCE_UPDATE = 0;
    private static long MIN_TIME_UPDATE = 1000 * 10 * 1;

    public UserLocation(Context context){
        mContext = context;
    }
    public void setLocation(){

        locationManager = (LocationManager)mContext.getSystemService(mContext.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions((Activity)mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions((Activity)mContext, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLongitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Location", "longtitude=" + lng + ", latitude=" + lat);
        }
        listProviders = locationManager.getAllProviders();
        boolean[] isEnable = new boolean[3];
        Log.d("setLocation", "for 직전");
        for (int i = 0; i < listProviders.size(); i++) {
            if (listProviders.get(i).equals(LocationManager.GPS_PROVIDER)) {
                isEnable[0] = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
            } else if (listProviders.get(i).equals(LocationManager.NETWORK_PROVIDER)) {
                isEnable[1] = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
            } else if (listProviders.get(i).equals(LocationManager.PASSIVE_PROVIDER)) {
                isEnable[2] = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
            }

        }

        Log.d("Location", listProviders.get(0) + '/' + String.valueOf(isEnable[0]));
        Log.d("Location", listProviders.get(1) + '/' + String.valueOf(isEnable[1]));
        Log.d("Location", listProviders.get(2) + '/' + String.valueOf(isEnable[2]));


    }

    @Override
    public void onLocationChanged(Location location) {
        // 위치 변경이 일어났을때 해야할 일
        // DB에 정보 갱신
        // 다음 갱신 시간 구하기
        double latitude = 0.0;
        double longitude = 0.0;

        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Location" + " GPS : ", Double.toString(latitude )+ '/' + Double.toString(longitude));
        }

        if(location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.d("Location" + " NETWORK : ", Double.toString(latitude )+ '/' + Double.toString(longitude));
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // 위치 정보 허용 상태가 바뀌었을때
        // 현재는 중앙의 하트의 색을 바꾸기로.
    }

    @Override
    public void onProviderEnabled(String provider) {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
