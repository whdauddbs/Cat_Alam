package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

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
                Intent intent=new Intent(getApplicationContext(), MainPage.class);
                startActivity(intent);
            }
        });
    }
    //사용자가 입력할 때마다 검사.
    TextWatcher textWatcher_u_name = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if ( u_name.getText().toString().length() == 0 ||
                    !Pattern.matches("/^[가-힣]{2,4}$/", (CharSequence) u_name.getText().toString())) {
                Log.d("my_register","유저 이름 에러");
                registertext.setText("유저 이름을 확인하세요");
                flag1=false;
            }
            else {
                flag1 = true;
                registertext.setText("");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            //입력 내용 검증
            if(flag1 && flag2 && flag4 && flag3) {
                button.setEnabled(true);
            }else {
                Log.d("my_register","다시 입력.");
                Log.d("my_register",flag1.toString());

            }
        }
    };
    TextWatcher textWatcher_u_pnum= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if ( u_pnumber.getText().toString().length() == 0 ||
                    !Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$", (CharSequence) u_pnumber.getText().toString())) {
                Log.d("my_register","유저 폰번호 에러");
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
            //입력 내용 검증
            if(flag1 && flag2 && flag4 && flag3) {
                button.setEnabled(true);
            }else {
                Log.d("my_register","다시 입력.");
            }
        }
    };
    TextWatcher textWatcher_y_pnum= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ( y_pnumber.getText().toString().length() == 0 ||
                    !Pattern.matches("^01(?:0|1|[6-9]) - (?:\\d{3}|\\d{4}) - \\d{4}$", (CharSequence) y_pnumber.getText().toString())) {
                Log.d("my_register","상대 폰번호 에러");
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
            //입력 내용 검증
            if(flag1 && flag2 && flag4 && flag3) {
                button.setEnabled(true);
            }else {
                Log.d("my_register","다시 입력.");
            }
        }
    };
    TextWatcher textWatcher_u_id= new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if ( u_id.getText().toString().length() == 0 ||
                    !Pattern.matches("/^[가-힣]{2,4}|[a-zA-Z]{2,10}\\s[a-zA-Z]{2,10}$/", (CharSequence) u_id.getText().toString())) {
                Log.d("my_register","유저 닉네임 에러");
                registertext.setText("유저 닉네임을 확인하세요.");
                flag4=false;

            }
            else{
                flag4=true;
                registertext.setText("");
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            //입력 내용 검증
            if(flag1 && flag2 && flag4 && flag3) {
                button.setEnabled(true);
            }else {
                Log.d("my_register","다시 입력.");
            }
        }
    };

    }
