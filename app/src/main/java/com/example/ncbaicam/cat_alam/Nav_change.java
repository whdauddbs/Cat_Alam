package com.example.ncbaicam.cat_alam;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Nav_change extends Fragment {
    View v;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        v=inflater.inflate(R.layout.nav_change, container, false);
        return v;
    }
    //확인 버튼 눌렀을 때, 뒤로가기 눌렀을 때 처리해야함
    //((MainPage)getActivity()).destroyFragment();
}
