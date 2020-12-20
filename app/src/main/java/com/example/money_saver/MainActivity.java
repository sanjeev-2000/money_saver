package com.example.money_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Animation top_anim , bottom_anim;
    TextView textView1,textView2;
    ImageView sp_bg;
    LottieAnimationView animationView;
    FirebaseAuth mAuth;

    private static int SPLASHTO = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top_anim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottom_anim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        sp_bg = findViewById(R.id.splash_bg);
        animationView = findViewById(R.id.animationView);

        // check if user is already logged in

        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);

        sp_bg.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        animationView.animate().translationY(-2000).setDuration(1000).setStartDelay(4000);
        textView1.animate().translationY(2000).setDuration(1000).setStartDelay(4000);
        textView2.animate().translationY(2000).setDuration(1000).setStartDelay(4000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // check if user is already logged in
                textView1.setAnimation(bottom_anim);
                textView2.setAnimation(bottom_anim);
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    Intent intent = new Intent(MainActivity.this,home_screen.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(MainActivity.this, login_activity.class);
                    startActivity(intent);
                }
                finish();
            }
        },SPLASHTO);

//        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
//                {
//                    Intent intent = new Intent(MainActivity.this,home_screen.class);
//                    startActivity(intent);
//                }
//                else {
//                    Intent intent = new Intent(MainActivity.this, login_activity.class);
//                    startActivity(intent);
//                }
//                finish();

    }
}