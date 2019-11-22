package com.example.ncbaicam.cat_alam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;


public class Nav_change extends Fragment {
    View v;
    TextView now_you_phone;
    EditText change_pn;
    Button change_btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.nav_change, container, false);

        change_pn=v.findViewById(R.id.change_pn);

        now_you_phone=v.findViewById(R.id.now_you_phone);
        now_you_phone.setText(showYpnum());

        change_btn=v.findViewById(R.id.chane_btn);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //번호 검사
                if ( change_pn.getText().length() == 0 ||
                        !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", change_pn.getText())) {
                    Toast toast = Toast.makeText(((MainPage)getActivity()), "좋아하는 사람의 폰번호를 확인하세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    //저장하고
                    saveChange_ypnum();
                    //뜨는 번호 바꾸고
                    now_you_phone.setText(showYpnum());
                }
            }
        });
        return v;
    }
    //바뀐 고객 번호 저장 함수
    public void saveChange_ypnum(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String y_pnumber_save = change_pn.getText().toString();
        editor.putString("y_pnumber",y_pnumber_save);

        editor.commit();
    }
    public String showYpnum(){
        SharedPreferences sharedPreferences =this.getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        //기존 저장된 로그 가져옴.
        String y_pnumber_saved = sharedPreferences.getString("y_pnumber","null");

        return y_pnumber_saved;
    }



}
