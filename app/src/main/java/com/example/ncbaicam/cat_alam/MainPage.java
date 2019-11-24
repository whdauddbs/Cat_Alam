package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ncbaicam.cat_alam.Background.LocationService;
import com.example.ncbaicam.cat_alam.Background.UndeadService;
import com.example.ncbaicam.cat_alam.Item.UserInfoItem;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserInfoItem user;
    TextView nav_username;
    UserLocation userLocation;
    BackPressCloseHandler backPressCloseHandler;
    Intent foregroundServiceIntent;

    //프레그먼트에 넘겨줄 스트링
    String mtime="";
    String mlat="";
    String mlng="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);

        //넘어온 유저 객체 설정
        user=(UserInfoItem)getIntent().getSerializableExtra("userInfoItem");
        // 네비게이션 드로어
        setNav();
        //header에 유저 이름 설정
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_username);
        navUsername.setText(user.name);

        //뒤로가기
        backPressCloseHandler = new BackPressCloseHandler(this);

        userLocation = new UserLocation(this, user.phone);
        userLocation.setLocation(); // 위치얻기

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        userLocation.stopLocation();
        setService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Service", "서비스 종료");
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                LocationService.class); // 이동할 컴포넌트
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        //뒤로 갈 때 회원가입 페이지로 넘어감.https://jinunthing.tistory.com/22 참고
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            backPressCloseHandler.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //좋아하는 사람 바꾸기
        if (id == R.id.nav_change) {
           fragment=new Nav_change();
        }
        //"나를 좋아하는 사람과 나의 최근 거리"
        else if (id == R.id.nav_alarm) {
            fragment=new Nav_alarm();
            ((Nav_alarm) fragment).alarm_cnt = userLocation.getCount();
        }
        //나를 좋아하는 사람과 마지막으로 만난 날짜
        else if (id == R.id.nav_meeting) {
            //여기서 스트링으로 보내주자.
            fragment=new Nav_meeting();
            //프레그먼트에 문자열 넘기는 함수
            stringToMeet();
            // TODO: 2019-11-22 그리드뷰 테스트
            /*
            ((Nav_meeting) fragment).stime=this.mtime;
            ((Nav_meeting) fragment).slat=this.mlat;
            ((Nav_meeting) fragment).slng=this.mlng;
            */
            ((Nav_meeting) fragment).stime="20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225;20190108;20190908;20201225";
            ((Nav_meeting) fragment).slat="41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338";
            ((Nav_meeting) fragment).slng="37.757687;128.873749;128.873749;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338;41.40338;2.17403;41.40338";
        }
        backPressCloseHandler.setFragment(fragment);
        ft.replace(R.id.content_fragment_layout, fragment);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

//유저 이름 설정
        nav_username = findViewById(R.id.nav_username);
        nav_username.setText(user.name);

        return true;
    }

    public void setNav(){
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //아이콘 클릭하면 네비 열림
        ImageButton openbtn=findViewById(R.id.open_heart);
        openbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        //네비바 열고 아이템 선택
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //넘겨주는 용도 함수
    public void stringToMeet(){
        SharedPreferences sharedPreferences = getSharedPreferences("Meet", MODE_PRIVATE);
        //기존 저장된 로그 가져옴.
        this.mtime=sharedPreferences.getString("mtime", "");
        this.mlat=sharedPreferences.getString("mlat", "");
        this.mlng=sharedPreferences.getString("mlng", "");

    }




    public void setService(){
        /*
        Log.d("SetService", "백그라운드 동작");
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(0, new ComponentName(this, GPS_Service.class))
                .setMinimumLatency(TimeUnit.SECONDS.toSeconds(5)) // 시간 바꿔야함
                .setOverrideDeadline(TimeUnit.MINUTES.toMillis(3))
                .build());
        */
        /*
        Log.d("Service", "서비스 시작");
        Intent intent = new Intent(
                getApplicationContext(),//현재제어권자
                LocationService.class); // 이동할 컴포넌트
        intent.putExtra("phone", user.phone);
        startService(intent);
        */
        if(UndeadService.serviceIntent == null){
            foregroundServiceIntent = new Intent(this, UndeadService.class);
            startService(foregroundServiceIntent);
            Log.d("Service", "setService: 서비스 시작");
        }
        else{
            foregroundServiceIntent = UndeadService.serviceIntent;
            Log.d("Service", "setService: 서비스 시작");
        }
    }
}
