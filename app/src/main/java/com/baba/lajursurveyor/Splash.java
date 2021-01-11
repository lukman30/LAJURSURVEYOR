package com.baba.lajursurveyor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baba.lajursurveyor.login.Login;
import com.baba.lajursurveyorsurveyor.R;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

//
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 5 seconds
                    sleep(3*1000);

                    // After 5 seconds redirect to another intent
                    Intent i=new Intent(getBaseContext(), Login.class);
                    startActivity(i);

                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        background.start();


    }



    @Override
    protected void onDestroy() {

        super.onDestroy();

    }
}