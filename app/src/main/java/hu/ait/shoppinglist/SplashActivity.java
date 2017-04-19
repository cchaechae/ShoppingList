package hu.ait.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final Animation anim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.slide_in);
        final ImageView ivlogo = (ImageView) findViewById(R.id.ivlogo);

        ivlogo.startAnimation(anim);

        Thread timeThread = new Thread(){

            @Override
            public void run() {
                try{
                    sleep(3000);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);

                    //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        };
        timeThread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
