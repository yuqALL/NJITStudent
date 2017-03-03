package com.njit.student.yuqzy.njitstudent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomActivity extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 500; //延迟2秒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent mainIntent = new Intent(WelcomActivity.this,MainActivity.class);
                WelcomActivity.this.startActivity(mainIntent);
                WelcomActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }
}
