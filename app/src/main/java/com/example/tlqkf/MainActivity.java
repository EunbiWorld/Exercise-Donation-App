package com.example.tlqkf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;

    TextView stepTextView;
    TextView tv_weight;
    TextView tv_kcal;
    Button btn_plus;
    Button btn_minus;

    int stepcount = 0;
    //저역 통과 필터용
    boolean first = true;
    boolean up = false;
    float d0, d= 0f;
    //필터링 계수
    float a = 0.65f;

    int Width, Height;
    int mWeight = 60;
    double calorie;
    static int walkingCount = 0;
    DecimalFormat format;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepTextView = (TextView) findViewById(R.id.counter);

        tv_weight = findViewById(R.id.tv_weight);
        tv_kcal = findViewById(R.id.tv_kcal);
        btn_plus = findViewById(R.id.btn_plus);
        btn_minus = findViewById(R.id.btn_minus);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        tv_weight.setText("몸무게: 60kg");

        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeight++;
                tv_weight.setText("몸무게: "+mWeight);
            }
        });

        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeight--;
                tv_weight.setText("몸무게: "+mWeight);

            }
        });
    }


    //센서 값이 업데이트될 때
    //@Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        // Y軸
        float y = event.values[1];
        // Z軸
        float z = event.values[2];

        float sum = (float) Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));

        if(first) {
            first = false;
            up = true;
            d0 = a*sum;
        } else {
            //저역 통과 필터링
            d = a*sum+(1-a)*d0;
            if(up&&d<d0){
                up=false;
                stepcount++;
            }else if(!up&& d>d0){
                up=true;
                //d0=d;
            }
            stepTextView.setText(String.valueOf(stepcount)+"걸음");
        }

        //format = new DecimalFormat("0.000");
        calorie = 0.04 * walkingCount;
        tv_kcal.setText("소비한 칼로리: " + String.valueOf(calorie) + " kcal");

    }

    @Override
    public void onAccuracyChanged (Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mAccelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 비활성시 SensorEvent를 취하지 않도록 리스너 등록 해제
        mSensorManager.unregisterListener(this);
    }



}