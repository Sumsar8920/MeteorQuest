package com.example.rasmus.p9.Minigames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rasmus.p9.NavigationMethod.Navigation;
import com.example.rasmus.p9.NavigationMethod.NavigationActivity;
import com.example.rasmus.p9.Other.GameScreen;
import com.example.rasmus.p9.R;

public class TreasureHunt extends AppCompatActivity implements SensorEventListener {

    SensorManager smAccelerometer;
    Sensor accelerometer;
    private float mGZ = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 1;
    MediaPlayer media;
    int counter = 0;
    String playerRole;
    ImageView image;
    MediaPlayer mediaPlayer;

    public Handler handler = new Handler();
    public int delay = 1000; //milliseconds
    int imageCounter = 0;
    ImageView shuffleImg;
    TextView text;

    public boolean stopAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        //setContentView(R.layout.activity_shuffle_game);

        SharedPreferences shared = getSharedPreferences("your_file_name", MODE_PRIVATE);
        playerRole = (shared.getString("PLAYERROLE", ""));

        mediaPlayer = MediaPlayer.create(this, R.raw.shuffle);

        //initialize sensor manager for accelerometer/navigation method
        smAccelerometer = (SensorManager) getSystemService(SENSOR_SERVICE);
        // MiniGameDrink sensor
        accelerometer = smAccelerometer.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Register sensor listener
        smAccelerometer.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        stopAnimation = false;

        image = (ImageView) findViewById(R.id.image);

        shuffleImg = (ImageView) findViewById(R.id.shuffleImg);

        text = (TextView) findViewById(R.id.textView3);

        if(stopAnimation == false) {
            changeImage();
        }

    }

    @Override
    public void onSensorChanged (SensorEvent event){
        try {
            int type = event.sensor.getType();
            if (type == Sensor.TYPE_ACCELEROMETER) {
                float gz = event.values[2];
                if (mGZ == 0) {
                    mGZ = gz;
                } else {
                    if ((mGZ * gz) < 0) {
                        mEventCountSinceGZChanged++;
                        if (mEventCountSinceGZChanged == MAX_COUNT_GZ_CHANGE) {
                            mGZ = gz;
                            mEventCountSinceGZChanged = 0;
                            if (gz > 0) {
                                //Log.d(TAG, "now screen is facing up.");
                                //Toast toast = Toast.makeText(getApplicationContext(), "Up", Toast.LENGTH_SHORT);
                                //toast.show();
                                stopAnimation = true;
                                displayImage();
                                mediaPlayer.start();


                            } else if (gz < 0) {
                                //Log.d(TAG, "now screen is facing down.");
                                //Toast toast = Toast.makeText(getApplicationContext(), "Down", Toast.LENGTH_SHORT);
                                //toast.show();
                                counter ++;


                            }
                        }
                    } else {
                        if (mEventCountSinceGZChanged > 0) {
                            mGZ = gz;
                            mEventCountSinceGZChanged = 0;
                        }
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    public void displayImage(){

        if(playerRole.equals("1")) {
            if (counter == 1) {
                image.setBackgroundResource(R.drawable.player1_8);
                shuffleImg.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }

            if (counter == 2) {
                image.setBackgroundResource(R.drawable.player1_5);
            }

            if (counter == 3) {
                image.setBackgroundResource(R.drawable.player1_3);

            }

            if (counter == 4) {
                image.setBackgroundResource(R.drawable.player1_2);
                //counter = 0;
                Intent intent = new Intent(TreasureHunt.this, NavigationActivity.class);
                startActivity(intent);
                Navigation.minigame2Done = true;
                Navigation.gameRunning = false;

            }
        }

        if(playerRole.equals("2")) {
            if (counter == 1) {
                image.setBackgroundResource(R.drawable.player2_2);
                shuffleImg.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }

            if (counter == 2) {
                image.setBackgroundResource(R.drawable.player2_5);
            }

            if (counter == 3) {
                image.setBackgroundResource(R.drawable.player2_8);

            }

            if (counter == 4) {
                image.setBackgroundResource(R.drawable.player2_3);
                counter = 0;

            }
        }

        if(playerRole.equals("3")) {
            if (counter == 1) {
                image.setBackgroundResource(R.drawable.player3_5);
                shuffleImg.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }

            if (counter == 2) {
                image.setBackgroundResource(R.drawable.player3_3);
            }

            if (counter == 3) {
                image.setBackgroundResource(R.drawable.player3_2);

            }

            if (counter == 4) {
                image.setBackgroundResource(R.drawable.player3_8);
                counter = 0;

            }
        }

        if(playerRole.equals("4")) {
            if (counter == 1) {
                image.setBackgroundResource(R.drawable.player4_2);
                shuffleImg.setVisibility(View.GONE);
                text.setVisibility(View.GONE);
            }

            if (counter == 2) {
                image.setBackgroundResource(R.drawable.player4_3);
            }

            if (counter == 3) {
                image.setBackgroundResource(R.drawable.player4_5);

            }

            if (counter == 4) {
                image.setBackgroundResource(R.drawable.player4_8);
                counter = 0;

            }
        }





    }

    public void changeImage(){
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                imageCounter ++;
                if (imageCounter == 1) {
                    shuffleImg.setImageResource(R.drawable.scoutfront);
                }
                if (imageCounter == 2) {
                    shuffleImg.setImageResource(R.drawable.scoutback);
                    imageCounter = 0;
                }


                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void fullscreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_shuffle_game);
    }

    @Override
    public void onBackPressed() {
        // your code.
        mediaPlayer.release();
        mediaPlayer = null;
        smAccelerometer.unregisterListener(this);
        Intent intent = new Intent(TreasureHunt.this, GameScreen.class);
        startActivity(intent);
    }

}