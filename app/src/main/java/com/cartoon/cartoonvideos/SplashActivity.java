package com.cartoon.cartoonvideos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.cartoon.cartoonvideos.R.id;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public final class SplashActivity extends AppCompatActivity {
   ImageView imageView;

    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(id.image2);

        //animating view
        YoYo.with(Techniques.Tada).duration(2000L).repeat(10).playOn(imageView);
        (new Handler()).postDelayed((Runnable)(new Runnable() {
            public final void run() {

                //starting tab activity
               startActivity(new Intent(SplashActivity.this, TabActivity.class));
               finish();
            }
        }), 2000L);
    }


}
