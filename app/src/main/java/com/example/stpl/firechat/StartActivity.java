package com.example.stpl.firechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {
private Button mloginbtn, mregbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        mloginbtn=(Button)findViewById(R.id.start_login_btn);
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent1=new Intent(StartActivity.this,LoginActivity.class);
                startActivity(reg_intent1);
            }
        });
        mregbtn=(Button)findViewById(R.id.start_reg_btn);
        mregbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg_intent=new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(reg_intent);
            }
        });
    }
}
