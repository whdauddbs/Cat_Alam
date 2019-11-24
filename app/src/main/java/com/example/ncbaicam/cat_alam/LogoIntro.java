package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ncbaicam.cat_alam.Item.UserInfoItem;

public class LogoIntro extends AppCompatActivity {
    SharedPreferences appData;
    //유저 이름
    String u_name;
    //유저 핸드폰 번호
    String u_pnumber;
    //상대 핸드폰 번호
    String y_pnumber;
    //유저 닉네임
    String u_id;
    UserInfoItem userInfoItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_intro);
        //회원가입 여부 확인
        if (chkRegister()){//가입되어 있으면 -> user 객체랑 메인 페이지로
            Log.d("my_intro", "가입됨. 메인으로 ");
            load();//읽어와서 객체에 저장
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    Log.d("my_intro",userInfoItem.name);
                    Intent intent = new Intent (getApplicationContext(), MainPage.class);
                    intent.putExtra("userInfoItem", userInfoItem);
                    startActivity(intent); //다음화면으로 넘어감
                    finish();
                }
            },1000); //3초 뒤에 Runner객체 실행하도록 함
        }
        else {//안되어 있으면 -> 회원 가입 페이지로
            Log.d("my_intro", "가입안됨. 회원가입으로");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable(){
                @Override
                public void run() {
                    Intent intent = new Intent (getApplicationContext(), Register.class);
                    startActivity(intent); //다음화면으로 넘어감
                    finish();
                }
            },1000); //3초 뒤에 Runner객체 실행하도록 함
        }

    }
    protected boolean chkRegister(){
        //user name 없음 -> 생성파일에 키-값 없음 -> 회원가입 안되어있음
        //user name 있음 -> 읽은 파일에 키-값 존재 -> 회원가입 되어있음 -> 객체에 유저 정보 담아서 넘김.
        appData = getSharedPreferences("Register", MODE_PRIVATE);
        //user name이 비어있을 경우 "null"반환
        String get=appData.getString("u_name", "null");
        if (get.equals("null")){
            Log.d("my_intro", "null임 ");
            Log.d("my_intro", get);
            return false;
        }
        else{
            return true;
        }
    }

    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값

        u_name= appData.getString("u_name", "null");
        u_pnumber = appData.getString("u_pnumber", "null");
        y_pnumber=appData.getString("y_pnumber", "null");
        u_id=appData.getString("u_id", "null");
        //정보 읽어와서 객체 생성
        userInfoItem =
                new UserInfoItem(
                        u_pnumber,
                        u_name,
                        u_id,
                        y_pnumber);
        Log.d("my_intro",userInfoItem.name);


    }

}
