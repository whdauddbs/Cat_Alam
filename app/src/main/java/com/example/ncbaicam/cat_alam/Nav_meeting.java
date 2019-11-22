package com.example.ncbaicam.cat_alam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class Nav_meeting extends Fragment{
    String stime="";
    String slat="";
    String slng="";
    View v;
    GridView gridView;
    //point 이미지
    ImageView point;
    //텍스트 배열 선언
    ArrayList<String> textArr = new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_meeting, container, false);
        point=v.findViewById(R.id.log_point);
        makeList();
        gridView = (GridView) v.findViewById(R.id.log_list);
        gridView.setAdapter(new listAdapter((MainPage)getActivity(), textArr, point));
        //gridView = v.findViewById(R.id.log_list);
        //gridView.setAdapter(new listAdapter(getActivity(), R.layout.log_layout, textArr, point));
        return v;
    }
    //여기서 string 해체해서 row 만들기
    public void makeList() {
        //분해해서 각자
        String[] atime = stime.split(";");
        String[] alat = slat.split(";");
        String[] alng = slng.split(";");
        String blank="        ";
        //문자열 만들어서 리스트에 넣기
        for (int i=0 ; i<atime.length ; i++)
        {
            textArr.add(atime[i] + blank +  "[ "+alat[i]  + " °N" + blank + alng[i] + " ° E ]" );
        }

    }

}
