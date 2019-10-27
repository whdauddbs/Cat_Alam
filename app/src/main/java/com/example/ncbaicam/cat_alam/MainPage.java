package com.example.ncbaicam.cat_alam;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    LocationManager locationManager;
    private List<String> listProviders;
    private static long MIN_DISTANCE_UPDATE = 0;
    private static long MIN_TIME_UPDATE = 1000 * 10 * 1;
    //ActionBar ab = getSupportActionBar() ;
    //툴바 문제있음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);

        setNav(); // 네비게이션 드로어
        setLocation(); // 위치얻기
        //ab.setTitle("") ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        setService(); // 꺼졌을때 위치 서비스 등록
    }

    @Override
    public void onBackPressed() {
        //뒤로 갈 때 회원가입 페이지로 넘어감.https://jinunthing.tistory.com/22 참고
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }



    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        getMenuInflater().inflate(R.menu.activity_menu_navigation_drawer, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_settings) {
        //    return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        if (id == R.id.nav_change) {
           fragment=new Nav_change();
            //ab.setTitle("좋아하는 사람 바꾸기") ;
        } else if (id == R.id.nav_distance) {
            fragment=new Nav_distance();
            //ab.setTitle("나를 좋아하는 사람과 나의 최근 거리") ;
        } else if (id == R.id.nav_meeting) {
            fragment=new Nav_meeting();
            //ab.setTitle("나를 좋아하는 사람과 마지막으로 만난 날짜") ;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void destroyFragment() {

    }

    public void setNav(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.getBackground().setAlpha(0);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 이름 안보이게

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.menu);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setLocation(){

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //권한이 없을 경우 최초 권한 요청 또는 사용자에 의한 재요청 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION) &&
                    ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // 권한 재요청
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
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
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setService(){
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(1234, new ComponentName(this, GPS_Service.class)).setPeriodic(10000).build()); // 시간 바꿔야함

    }
}
