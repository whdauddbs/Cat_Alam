package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LogoIntro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_intro);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), Register.class);
                startActivity(intent); //다음화면으로 넘어감
                finish();
            }
        },2000); //3초 뒤에 Runner객체 실행하도록 함
    }
}
