package com.example.ncbaicam.cat_alam;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserInfoItem user;
    TextView nav_username;
    UserLocation userLocation = new UserLocation(this);
    BackPressCloseHandler backPressCloseHandler;
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


        userLocation.setLocation(); // 위치얻기
    }


    @Override
    protected void onPause() {
        super.onPause();
        //setService(); // 꺼졌을때 위치 서비스 등록
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

//유저 이름 설정
        nav_username = findViewById(R.id.nav_username);
        nav_username.setText(user.name);

        return true;
    }
    public void destroyFragment() {

    }

    public void setNav(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();


        //actionBar.setDisplayHomeAsUpEnabled(true);//뒤로가기
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //toolbar.getBackground().setAlpha(0);
        //getSupportActionBar().setDisplayShowTitleEnabled(false); // 타이틀 이름 안보이게

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //아이콘 클릭하면 네비 열림
        ImageButton openbtn=findViewById(R.id.nav_open);
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

    public void setService(){
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(1234, new ComponentName(this, GPS_Service.class)).setPeriodic(10000).build()); // 시간 바꿔야함

    }
}
