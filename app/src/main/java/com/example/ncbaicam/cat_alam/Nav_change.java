package com.example.ncbaicam.cat_alam;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ncbaicam.cat_alam.Item.UserInfoItem;
import com.example.ncbaicam.cat_alam.remote.RemoteService;
import com.example.ncbaicam.cat_alam.remote.ServiceGenerator;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Nav_change extends Fragment {
    View v;
    TextView now_you_phone;
    EditText change_pn;
    Button change_btn;
    SharedPreferences sharedPreferences;
    UserInfoItem userInfoItem;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.nav_change, container, false);

        change_pn=v.findViewById(R.id.change_pn);
        now_you_phone=v.findViewById(R.id.now_you_phone);
        change_btn=v.findViewById(R.id.chane_btn);

        loadUserInfo();
        now_you_phone.setText(userInfoItem.youPhone);
        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //번호 검사
                if ( change_pn.getText().length() == 0 ||
                        !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", change_pn.getText())) {
                    Toast toast = Toast.makeText(((MainPage)getActivity()), "좋아하는 사람의 폰번호를 확인하세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //기존 번호랑 같으면
                else if(change_pn.getText().toString().equals(now_you_phone.getText().toString())){
                    Toast toast = Toast.makeText(((MainPage)getActivity()), "이미 등록된 번호입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    //저장하고
                    saveChange_ypnum();
                    //뜨는 번호 바꾸고
                    now_you_phone.setText(userInfoItem.youPhone);
                    saveDB();
                    Toast toast = Toast.makeText(((MainPage)getActivity()), "변경되었습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        return v;
    }
    //바뀐 고객 번호 저장 함수
    public void saveChange_ypnum(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String y_pnumber_save = change_pn.getText().toString();
        userInfoItem.youPhone = y_pnumber_save;
        editor.putString("y_pnumber",y_pnumber_save);

        editor.commit();
    }
    public void loadUserInfo(){
        sharedPreferences =this.getActivity().getSharedPreferences("Register", Context.MODE_PRIVATE);
        //기존 저장된 로그 가져옴.
        userInfoItem = new UserInfoItem(sharedPreferences.getString("u_pnumber","null"),
                sharedPreferences.getString("u_name","null"),
                sharedPreferences.getString("u_id","null"),
                sharedPreferences.getString("y_pnumber","null"));
        return;
    }

    public void saveDB(){
        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.changeYouPhone(userInfoItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String seq = response.body();
                    try{
                        if(seq == ""){
                            Log.d("changeDB", "change Fail");
                            return;
                        }
                    } catch (Exception e) {
                        Log.d("changeDB", "change Fail");
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("changeDB","통신 실패");
            }
        });
    }

}
