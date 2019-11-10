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
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    UserLocation userLocation = new UserLocation(this);
    //ActionBar ab = getSupportActionBar() ;
    //툴바 문제있음
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_navigation);
        setNav(); // 네비게이션 드로어
        userLocation.setLocation(); // 위치얻기
        //ab.setTitle("") ;
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



    public void setService(){
        JobScheduler jobScheduler =(JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(new JobInfo.Builder(1234, new ComponentName(this, GPS_Service.class)).setPeriodic(10000).build()); // 시간 바꿔야함

    }
}
