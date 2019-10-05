package com.example.ncbaicam.cat_alam;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        FloatingActionButton button=findViewById(R.id.menu_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(getApplicationContext(), Cat_Menu.class);
                //startActivity(intent);
                Toast.makeText(MainPage.this, "여기에 설정 창 만들어야함", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
