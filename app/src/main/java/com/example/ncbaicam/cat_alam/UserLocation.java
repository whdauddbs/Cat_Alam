package com.example.ncbaicam.cat_alam;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.ncbaicam.cat_alam.Item.ResponseBody;
import com.example.ncbaicam.cat_alam.Item.UserInfoItem;
import com.example.ncbaicam.cat_alam.Item.UserLocationItem;
import com.example.ncbaicam.cat_alam.remote.RemoteService;
import com.example.ncbaicam.cat_alam.remote.ServiceGenerator;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class UserLocation implements LocationListener {
    private Context mContext;
    LocationManager locationManager;
    private List<String> listProviders;
    private static long MIN_DISTANCE_UPDATE = 100;
    private static long MIN_TIME_UPDATE = 1000 * 10 * 1;
    private static String phoneNumber;
    private int ringing_cnt = 0;

    public UserLocation(Context context, String phoneNumber){
        mContext = context; this.phoneNumber = phoneNumber;
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
        Log.d("Location" , "location Changed********");
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

        saveDB(latitude, longitude); // DB에 위치 저장하고 상대거리 파악

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

    public void saveDB(final double lat, final double lng){
        final UserLocationItem newItem = new UserLocationItem(this.phoneNumber, lng, lat);

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<ResponseBody> call = remoteService.insertLocation(newItem);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    double distance = response.body().distance * 1000;
                    String status = response.body().status;
                    try{
                        if(status== "error"){
                            Log.d("saveDB", "Save Fail");
                            return;
                        }
                    } catch (Exception e){
                        Log.d("saveDB", "Save Fail");
                        return;
                    }

                    // 단위 : m
                    //*****3m 안에 들어오면 알림*******
                    if(distance < 3) {
                        //정보 저장
                        saveMeet(lat, lng);
                        Vibrator vibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    public void stopLocation(){
        Log.d("Location", "액티비티 위치 갱신 정지");
        locationManager.removeUpdates(this);
    }

    //알람 울렸을 때 meeting log 저장
    public void saveMeet(double lat, double lng){
        //SharedPreferences를 sFile이름, 기본모드로 설정
        //기존 저장된 로그
        String stime;
        String slat;
        String slng;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Meet", MODE_PRIVATE);
        //기존 저장된 로그 가져옴.
        stime=sharedPreferences.getString("mtime", "");
        slat=sharedPreferences.getString("mlat", "");
        slng=sharedPreferences.getString("mlng", "");

        //기존 로그 + 새로운 알림 로그(m~) 추가
        SimpleDateFormat format2 = new SimpleDateFormat ( "yyyy년 MM월dd일 HH시mm분");
        Date time = new Date();
        String ttime = format2.format(time);

        stime=stime+";"+ttime;
        slat=slat+";"+ Double.toString(lat);
        slng=slng+";"+Double.toString(lng);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mtime",stime); // key, value를 이용하여 저장하는 형태
        editor.putString("mlat",slat);
        editor.putString("mlng",slng);

        //최종 커밋
        editor.commit();
    }

    public int getCount(){
        return ringing_cnt;
    }

    //상대 알람 울리게 했을 때,
    // TODO: 2019-11-22 여기 백엔드랑 만들어야함. 

}
