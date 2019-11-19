package com.example.ncbaicam.cat_alam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Nav_alarm extends Fragment {
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_alarm, container, false);
        return v;
    }

    /*
    * if ( i == 0 ){
                    imageview.setImageResource(R.drawable.mountain); 이렇게 이미지 변경
    * */
}
