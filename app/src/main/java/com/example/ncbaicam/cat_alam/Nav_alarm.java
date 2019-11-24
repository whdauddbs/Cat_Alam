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

import org.w3c.dom.Text;

public class Nav_alarm extends Fragment {
    View v;
    ImageView alarm_img;
    TextView alarm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_alarm, container, false);
        alarm=v.findViewById(R.id.alarm_txt);
        alarm_img=v.findViewById(R.id.alarm_img);
        // TODO: 2019-11-24 구현 완성 되면 주석 해제 
        showalarm();
        return v;
    }

    public void showalarm(){
        //txt설정 - 변수로 받아오기
        SharedPreferences sharedPreferences =this.getActivity().getSharedPreferences("Alarm", Context.MODE_PRIVATE);
        String salarm = sharedPreferences.getString("alarm","0");
        alarm.setText("좋아하는 사람에게 "+salarm+"번 알람을 울렸습니다.");


        //이미지 설정
        int cnt_alarm = Integer.parseInt(String.valueOf(alarm.getText()));
        if (cnt_alarm==0){
            alarm_img.setImageResource(R.drawable.icon_0);
        }
        else if(cnt_alarm<5){
            alarm_img.setImageResource(R.drawable.icon_5);
        }
        else if(cnt_alarm<10){
            alarm_img.setImageResource(R.drawable.icon_10);
        }
        else if(cnt_alarm<20){
            alarm_img.setImageResource(R.drawable.icon_20);
        }
        else if(cnt_alarm<40){
            alarm_img.setImageResource(R.drawable.icon_40);
        }
        else if(cnt_alarm<60){
            alarm_img.setImageResource(R.drawable.icon_60);
        }
        else if(cnt_alarm<80){
            alarm_img.setImageResource(R.drawable.icon_80);
        }
        else{
            alarm_img.setImageResource(R.drawable.icon_100);
        }

    }

}
