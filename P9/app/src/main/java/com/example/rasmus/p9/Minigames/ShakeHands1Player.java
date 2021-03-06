package com.example.rasmus.p9.Minigames;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;

import android.os.Handler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rasmus.p9.NavigationMethod.Navigation;
import com.example.rasmus.p9.NavigationMethod.NavigationActivity;
import com.example.rasmus.p9.Other.Victory;
import com.example.rasmus.p9.R;

import java.util.ArrayList;
import java.util.List;

public class ShakeHands1Player extends AppCompatActivity implements SensorEventListener {

    public Sensor mySensor;
    public SensorManager SM;
    ImageButton button1;
    ImageButton button2;
    Boolean player1Ready = false;
    Boolean player2Ready = false;
    int counter = 0;
    int imageCounter = 0;
    ImageView middleImage;
    MediaPlayer mediaPlayer;
    TextView txt1, txt2;
    public Handler handler = new Handler();
    public int delay = 1000; //milliseconds
    public float zFloatScore;
    public float yFloatScore;
    List<Float> zFloat = new ArrayList<Float>();
    List<Float> yFloat = new ArrayList<Float>();
    public boolean givePoint = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullscreen();
        //setContentView(R.layout.activity_shake_hands_1player);

        button1 = (ImageButton)findViewById(R.id.button1);
        button2 = (ImageButton)findViewById(R.id.button2);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);

       /* txt1.setText("Place thumb on marker");
        txt2.setText("Place thumb on marker");*/

        middleImage = (ImageView) findViewById(R.id.animationDown);

        mediaPlayer = MediaPlayer.create(this, R.raw.batterycharge);


        // Create sensor manager
        SM = (SensorManager) getSystemService(SENSOR_SERVICE);

        // MiniGameDrink sensor
        mySensor = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        changeImage();
        //changeThumbImg();

        checkValuesInArrays();

        button1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        player1Ready = true;
                        button1.setBackgroundResource(R.drawable.greenthumb);
                        if (player1Ready == true && player2Ready == true){
                            // Register sensor listener
                            SM.registerListener(ShakeHands1Player.this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
                            middleImage.setImageResource(R.drawable.battery11player);
                            txt1.setText("SHAKE!");
                            txt2.setText("SHAKE!");
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        SM.unregisterListener(ShakeHands1Player.this);
                        counter = 0;
                        txt1.setText("");
                        txt2.setText("");
                        //so the players can start over if someone fails. They just have to release the button and press it again.
                        //txt1.setText("Place thumb on marker");
                        player1Ready = false;
                        button1.setBackgroundResource(R.drawable.thumb_scanner);
                        return true;
                }
                return false;
            }
        });

        button2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        player2Ready = true;
                        button2.setBackgroundResource(R.drawable.greenthumb);
                        if (player1Ready == true && player2Ready == true){
                            // Register sensor listener
                            SM.registerListener(ShakeHands1Player.this, mySensor, SensorManager.SENSOR_DELAY_NORMAL);
                            middleImage.setImageResource(R.drawable.battery11player);
                            txt1.setText("SHAKE!");
                            txt2.setText("SHAKE!");
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        // End
                        SM.unregisterListener(ShakeHands1Player.this);
                        counter = 0;
                        //txt1.setText("");
                        txt2.setText("");
                        //so the players can start over if someone fails. They just have to release the button and press it again.
                        //txt2.setText("Place thumb on marker");
                        player2Ready = false;
                        button2.setBackgroundResource(R.drawable.thumb_scanner);
                        return true;
                }
                return false;
            }
        });


    }

    public void checkValuesInArrays(){
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                //do something

                for(int i = 0; i < zFloat.size(); i++) {
                    yFloatScore = yFloat.get(i);
                    zFloatScore = zFloat.get(i);

                    if (yFloatScore > 10) {
                        counter = counter + 5;
                        //givePoint = true;
                        break;
                    }

                    if (i == zFloat.size() /*&& givePoint != true*/) {
                        counter--;
                        if (counter < 0) {
                            counter = 0;
                        }
                    }

                    givePoint = false;

                }
                txt1.setText(String.valueOf(counter));
                zFloat.clear();
                yFloat.clear();

                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        yFloat.add(event.values[1]);
        zFloat.add(event.values[2]);
        //float yFloat = event.values[1];
        //float zFloat = event.values[2];

       /* if(zFloat > 25 && yFloat > -2 && yFloat < 2) {
            counter ++;
            txt1.setText(String.valueOf(counter));
        } */

        /*if(counter == 4){
            counter ++;
            middleImage.setImageResource(R.drawable.battery21player);
            mediaPlayer.start();
        }

        if(counter == 10){
            counter ++;
            middleImage.setImageResource(R.drawable.battery31player);
            mediaPlayer.start();
        }

        if(counter == 20){
            counter ++;
            middleImage.setImageResource(R.drawable.battery41player);
            mediaPlayer.start();
        }

        if(counter == 30){
            counter ++;
            middleImage.setImageResource(R.drawable.battery51player);
            mediaPlayer.start();
            victory();
        } */


    }

    public void changeImage(){
        handler.postDelayed(new Runnable(){
            public void run(){
                //do something
                imageCounter ++;
                if(player1Ready == false || player2Ready == false) {
                    if (imageCounter == 1) {
                        middleImage.setImageResource(R.drawable.battery_singleplayerdown);
                    }
                    if (imageCounter == 2) {
                        middleImage.setImageResource(R.drawable.battery_singleplayerup);
                        imageCounter = 0;
                    }
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
    }


    public void victory(){
        Intent intent = new Intent(ShakeHands1Player.this, NavigationActivity.class);
        startActivity(intent);
        Navigation.minigame1Done = true;
        Navigation.gameRunning = false;
    }

    public void fullscreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_shake_hands_1player);
    }


}
