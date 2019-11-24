package com.example.ncbaicam.cat_alam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class Nav_alarm extends Fragment {
    View v;
    ImageView alarm_img;
    TextView alarm;
    public int alarm_cnt = 100;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_alarm, container, false);
        alarm=v.findViewById(R.id.alarm_txt);
        alarm_img=v.findViewById(R.id.alarm_img);
        // TODO: 2019-11-24 구현 완성 되면 주석 해제 
        showaAlarm();
        return v;
    }

    public void showaAlarm(){
        //txt설정 - 변수로 받아오기
        alarm.setText("좋아하는 사람에게 "+alarm_cnt+"번 알람을 울렸습니다.");

        //이미지 설정
        if (alarm_cnt==0){
            alarm_img.setImageResource(R.drawable.icon_0);
        }
        else if(alarm_cnt<5){
            alarm_img.setImageResource(R.drawable.icon_5);
        }
        else if(alarm_cnt<10){
            alarm_img.setImageResource(R.drawable.icon_10);
        }
        else if(alarm_cnt<20){
            alarm_img.setImageResource(R.drawable.icon_20);
        }
        else if(alarm_cnt<40){
            alarm_img.setImageResource(R.drawable.icon_40);
        }
        else if(alarm_cnt<60){
            alarm_img.setImageResource(R.drawable.icon_60);
        }
        else if(alarm_cnt<80){
            alarm_img.setImageResource(R.drawable.icon_80);
        }
        else{
            alarm_img.setImageResource(R.drawable.icon_100);
        }

    }

}
