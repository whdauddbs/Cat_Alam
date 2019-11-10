package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ncbaicam.cat_alam.remote.RemoteService;
import com.example.ncbaicam.cat_alam.remote.ServiceGenerator;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {
    //user 정보 객체
    UserInfoItem userInfoItem;

    //내용 체크용 변수
    Boolean flag1= false, flag2= false, flag3= false, flag4 = false;

    //제출버튼
    Button button;
    //정보 입력 에러 안내 뷰
    TextView registertext;
    //유저 이름
    EditText u_name;
    //유저 핸드폰 번호
    EditText u_pnumber;
    //상대 핸드폰 번호
    EditText y_pnumber;
    //유저 닉네임
    EditText u_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //제출버튼
        button = findViewById(R.id.register);
        //정보 입력 에러 안내 뷰
        registertext=findViewById(R.id.registertext);
        //유저 이름
        u_name = findViewById(R.id.user_name);
        //유저 핸드폰 번호
        u_pnumber = findViewById(R.id.user_pnumber);
        //상대 핸드폰 번호
        y_pnumber=findViewById(R.id.you_pnumber);
        //유저 닉네임
        u_id=findViewById(R.id.user_id);


        //일단 비활성화
        button.setEnabled(false);
        u_name.addTextChangedListener(textWatcher_u_name); // TextWatcher 리스너 등록
        u_pnumber.addTextChangedListener(textWatcher_u_pnum); // TextWatcher 리스너 등록
        y_pnumber.addTextChangedListener(textWatcher_y_pnum); // TextWatcher 리스너 등록
        u_id.addTextChangedListener(textWatcher_u_id); // TextWatcher 리스너 등록
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //기기에 유저 정보 저장
                saveUserInfo();
                //서버에 유저 정보 저장
                saveDB();
                //메인 화면으로
                Intent intent=new Intent(getApplicationContext(), MainPage.class);
                intent.putExtra("userInfoItem", userInfoItem);
                startActivity(intent);
            }
        });
    }
    //사용자가 입력할 때마다 검사.
    TextWatcher textWatcher_u_name = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            chkFlag();

            if ( s.toString().length() == 0 ||
                    !Pattern.matches("^[가-힣]{2,4}$", s)) {
                flag1=false;
            }
            else {
                flag1 = true;
                registertext.setText("");
            }
            Log.d("my_register",flag1.toString());
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            chkFlag();
        }
    };
    TextWatcher textWatcher_u_pnum= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            chkFlag();

            if ( s.length() == 0 ||
                    !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$",s)) {
                registertext.setText("유저 폰번호를 확인하세요.");
                flag2=false;

            }
            else{
                flag2=true;
                registertext.setText("");

            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            chkFlag();
        }
    };
    TextWatcher textWatcher_y_pnum= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            chkFlag();

            if ( s.length() == 0 ||
                    !Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", s)) {
                registertext.setText("좋아하는 사람의 폰번호를 확인하세요.");
                flag3=false;

            }
            else{
                flag3=true;
                registertext.setText("");
            }


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            chkFlag();
        }
    };
    TextWatcher textWatcher_u_id= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0 ||s.length() > 10 ) {
                registertext.setText("유저 닉네임을 확인하세요.");
                flag4 = false;

            } else {
                flag4 = true;
                registertext.setText("");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            chkFlag();
        }};
    public void chkFlag(){
        //입력 내용 검증
        if (flag1 && flag2 && flag4 && flag3) {
            button.setEnabled(true);
        }
    }
    public void saveUserInfo(){
        //SharedPreferences를 sFile이름, 기본모드로 설정
        SharedPreferences sharedPreferences = getSharedPreferences("Register",MODE_PRIVATE);

        //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String u_name_save = u_name.getText().toString(); // 사용자가 입력한 저장할 데이터
        editor.putString("u_name",u_name_save); // key, value를 이용하여 저장하는 형태
        String u_pnumber_save = u_pnumber.getText().toString();
        editor.putString("u_pnumber",u_pnumber_save);
        String y_pnumber_save = y_pnumber.getText().toString();
        editor.putString("y_pnumber",y_pnumber_save);
        String u_id_save = u_id.getText().toString();
        editor.putString("u_id",u_id_save);

        //최종 커밋
        editor.commit();
    }



    public void saveDB(){
        final UserInfoItem newItem = new UserInfoItem(u_pnumber.getText().toString(), u_name.getText().toString(), u_id.getText().toString(), y_pnumber.getText().toString());

        RemoteService remoteService = ServiceGenerator.createService(RemoteService.class);

        Call<String> call = remoteService.insertUserInfo(newItem);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String seq = response.body();
                    try{
                        if(seq == ""){
                            Log.d("saveDB", "regester Fail");
                            return;
                        }
                    } catch (Exception e){
                        Log.d("saveDB", "regester Fail");
                        return;
                    }
                    userInfoItem.phone = newItem.phone;
                    userInfoItem.name = newItem.name;
                    userInfoItem.nickname = newItem.nickname;
                    userInfoItem.youPhone = newItem.youPhone;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    }
