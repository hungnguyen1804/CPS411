package com.example.cps_lab411;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class OptionsActivity extends AppCompatActivity {

    Button mFly;
    Button mVirtualCabin;
    Button mSetting;
    Button mSettings;

    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFly = (Button) findViewById(R.id.button_fly);
        mVirtualCabin = (Button) findViewById(R.id.button_3d_obj);
        mSetting = (Button) findViewById(R.id.button_setting);

        mFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OptionsActivity.this, MainActivity.class));
            }
        });

        mVirtualCabin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

//        mSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(OptionsActivity.this, .class));
//            }
//        });


    }
}