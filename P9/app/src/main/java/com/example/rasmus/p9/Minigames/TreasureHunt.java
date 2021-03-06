package com.example.rasmus.p9.Minigames;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rasmus.p9.NavigationMethod.Navigation;
import com.example.rasmus.p9.NavigationMethod.NavigationActivity;
import com.example.rasmus.p9.Other.Database;
import com.example.rasmus.p9.Other.GameCompleted;
import com.example.rasmus.p9.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
public class TreasureHunt extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    private float mGZ = 0;//gravity acceleration along the z axis
    private int mEventCountSinceGZChanged = 0;
    private static final int MAX_COUNT_GZ_CHANGE = 1;
    MediaPlayer media;
    int counter = 1;
    String playerRole;
    ImageView background;
    MediaPlayer mediaPlayer;
    //ArrayList<Integer> arrayImage = new ArrayList<Integer>();
    Integer[] arrayImagePlayer1 = new Integer[5];
    Integer[] arrayImagePlayer2 = new Integer[5];
    Integer[] arrayImagePlayer3 = new Integer[5];
    Integer[] arrayImagePlayer4 = new Integer[5];

    Integer[] arrayImagePlayer1Blur = new Integer[5];
    Integer[] arrayImagePlayer2Blur = new Integer[5];
    Integer[] arrayImagePlayer3Blur = new Integer[5];
    Integer[] arrayImagePlayer4Blur = new Integer[5];



    public Handler handler = new Handler();
    public int delay = 1000; //milliseconds
    int imageCounter = 0;
    ImageView shuffleImg;
    TextView text;
    Button pinCodeButton;
    int counterShowButton = 0;
    public FirebaseDatabase database;
    public DatabaseReference rootReference;

    public static DecimalFormat DECIMAL_FORMATTER;
    public boolean imageShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_game);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //fullscreen();


        //startTimer();

        arrayImagePlayer1[1] = R.drawable.player1_2; arrayImagePlayer2[1] = R.drawable.player2_3; arrayImagePlayer3[1] = R.drawable.player3_8; arrayImagePlayer4[1] = R.drawable.player4_5;
        arrayImagePlayer1[2] = R.drawable.player1_3; arrayImagePlayer2[2] = R.drawable.player2_5; arrayImagePlayer3[2] = R.drawable.player3_2; arrayImagePlayer4[2] = R.drawable.player4_2;
        arrayImagePlayer1[3] = R.drawable.player1_5; arrayImagePlayer2[3] = R.drawable.player2_2; arrayImagePlayer3[3] = R.drawable.player3_5; arrayImagePlayer4[3] = R.drawable.player4_8;
        arrayImagePlayer1[4] = R.drawable.player1_8; arrayImagePlayer2[4] = R.drawable.player2_8; arrayImagePlayer3[4] = R.drawable.player3_3; arrayImagePlayer4[4] = R.drawable.player4_3;

        arrayImagePlayer1Blur[1] = R.drawable.player1_2_blur; arrayImagePlayer2Blur[1] = R.drawable.player2_3_blur; arrayImagePlayer3Blur[1] = R.drawable.player3_8_blur; arrayImagePlayer4Blur[1] = R.drawable.player4_5_blur;
        arrayImagePlayer1Blur[2] = R.drawable.player1_3_blur; arrayImagePlayer2Blur[2] = R.drawable.player2_5_blur; arrayImagePlayer3Blur[2] = R.drawable.player3_2_blur; arrayImagePlayer4Blur[2] = R.drawable.player4_2_blur;
        arrayImagePlayer1Blur[3] = R.drawable.player1_5_blur; arrayImagePlayer2Blur[3] = R.drawable.player2_2_blur; arrayImagePlayer3Blur[3] = R.drawable.player3_5_blur; arrayImagePlayer4Blur[3] = R.drawable.player4_8_blur;
        arrayImagePlayer1Blur[4] = R.drawable.player1_8_blur; arrayImagePlayer2Blur[4] = R.drawable.player2_8_blur; arrayImagePlayer3Blur[4] = R.drawable.player3_3_blur; arrayImagePlayer4Blur[4] = R.drawable.player4_3_blur;


        SharedPreferences shared = getSharedPreferences("your_file_name", MODE_PRIVATE);
        playerRole = (shared.getString("PLAYERROLE", ""));

        mediaPlayer = MediaPlayer.create(this, R.raw.shuffle);

        //initialize sensor manager for accelerometer/navigation method
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // MiniGameDrink sensor
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Register sensor listener
        //sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        background = (ImageView) findViewById(R.id.image);

        // define decimal formatter
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMATTER = new DecimalFormat("#.000", symbols);

        rootReference = Database.getDatabaseRootReference();
        DatabaseReference soundPuzzleReference = rootReference.child("treasurehunt");
        soundPuzzleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String key = ds.getKey().toString();
                    String value = ds.getValue().toString();
                    if(key.equals("soundfile1") && value.equals("true")){
                        //play soundfile 1
                        playSoundfile(key);
                        break;
                    }
                    if(key.equals("soundfile2") && value.equals("true")){
                        //play soundfile 2
                        playSoundfile(key);
                        break;
                    }

                    if(key.equals("stop") && value.equals("true")){
                        //play soundfile 2
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(TreasureHunt.this, R.raw.tada);
                        mediaPlayer.start();
                        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    mediaPlayer.stop();
                                    mediaPlayer.release();
                                    Intent intent = new Intent(TreasureHunt.this, GameCompleted.class);
                                    startActivity(intent);
                                }
                            });


                        break;
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged (SensorEvent event){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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
                                            mediaPlayer.start();

                                    } else if (gz < 0) {
                                        counter++;

                                            if (counter == 5) {
                                                counter = 1;
                                        }
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            // get values for each axes X,Y,Z
            float magX = event.values[0];
            float magY = event.values[1];
            float magZ = event.values[2];
            double magnitude = Math.sqrt((magX * magX) + (magY * magY) + (magZ * magZ));
            // set value on the screen
            //value.setText(DECIMAL_FORMATTER.format(magnitude) + " \u00B5Tesla");
            try {
                if (magnitude > 90 && imageShowing == false) {
                    imageShowing = true;

                    if (playerRole.equals("1")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer1[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (playerRole.equals("2")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer2[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (playerRole.equals("3")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer3[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (playerRole.equals("4")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer4[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (magnitude < 90) {
                    imageShowing = false;
                    if (playerRole.equals("1")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer1Blur[counter]);
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }
                    }
                    if (playerRole.equals("2")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer2Blur[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (playerRole.equals("3")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer3Blur[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (playerRole.equals("4")) {
                        try {
                            background.setBackgroundResource(arrayImagePlayer4Blur[counter]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

    public void startTimer(){
        //180000 milliseconds = 3 min
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                //mTextField.setText("done!");
                //Players "finish" the game when the time runs out
                Navigation.minigame2Done = true;
                Navigation.gameRunning = false;
                Intent intent = new Intent(TreasureHunt.this, NavigationActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    public void playSoundfile(String soundfile){
        mediaPlayer.stop();
        mediaPlayer.release();
        if(soundfile.equals("soundfile1")){
            mediaPlayer = MediaPlayer.create(this, R.raw.treasure_hunt_help_flip);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(TreasureHunt.this, R.raw.shuffle);
                }

            });
        }

        if(soundfile.equals("soundfile2")){
            mediaPlayer = MediaPlayer.create(this, R.raw.treasure_hunt_help_metal);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(TreasureHunt.this, R.raw.shuffle);
                }
            });
        }
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
        //smAccelerometer.unregisterListener(this);
        Intent intent = new Intent(TreasureHunt.this, NavigationActivity.class);
        startActivity(intent);
    }

}
