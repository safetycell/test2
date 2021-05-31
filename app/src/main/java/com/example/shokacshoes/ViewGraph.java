package com.example.shokacshoes;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ViewGraph extends AppCompatActivity {

    List<Float> temp = new ArrayList<Float>() {
        {
            add((float) 116.0);
            add((float) 111.0);
            add((float) 112.0);
            add((float) 121.0);
            add((float) 102.0);
            add((float) 83.0);
            add((float) 99.0);;
            add((float) 82.0);
        }
    };
    List<Float> temp2 = new ArrayList<Float>() {
        {
            add((float) 458.0072);
            add((float) 469.94232);
            add((float) 459.89044);
            add((float) 435.24927);
            add((float) 462.40717);
            add((float) 449.3659);
            add((float) 456.78046);
        }
    };
    private LineChart groundReactionL;
    private LineChart groundReactionR;
    private LineChart propulsionL;
    private LineChart propulsionR;
    private LineChart stillnessL;
    private LineChart stillnessR;
    private LineChart leftRightMotionL;
    private LineChart leftRightMotionR;
    private LineChart legStatusL;
    private LineChart legStatusR;
    DatabaseJson test;
    GraphVeiwModule groundReactionModuleL;
    GraphVeiwModule groundReactionModuleR;
    GraphVeiwModule propulsionModuleL;
    GraphVeiwModule propulsionModuleR;
    GraphVeiwModule stillnessModuleR ;
    GraphVeiwModule stillnessModuleL ;
    GraphVeiwModule leftRightMotionModuleL;
    GraphVeiwModule leftRightMotionModuleR ;
    GraphVeiwModule legStatusModuleL ;
    GraphVeiwModule legStatusModuleR ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.analysis_graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //GUIチャートの呼び起こし
        groundReactionL = findViewById(R.id.groundReactionL);
        groundReactionR = findViewById(R.id.groundReactionR);
        propulsionL = findViewById(R.id.propulsionL);
        propulsionR = findViewById(R.id.propulsionR);
        stillnessL = findViewById(R.id.stillnessL);
        stillnessR = findViewById(R.id.stillnessR);
        leftRightMotionL = findViewById(R.id.leftRightMotionL);
        leftRightMotionR = findViewById(R.id.leftRightMotionR);
        legStatusL = findViewById(R.id.legStatusL);
        legStatusR = findViewById(R.id.legStatusR);


        groundReactionModuleL = new GraphVeiwModule(groundReactionL);
        groundReactionModuleR = new GraphVeiwModule(groundReactionR);
        propulsionModuleL = new GraphVeiwModule(propulsionL);
        propulsionModuleR = new GraphVeiwModule(propulsionR);
        stillnessModuleR = new GraphVeiwModule(stillnessL);
        stillnessModuleL = new GraphVeiwModule(stillnessR);
        leftRightMotionModuleL = new GraphVeiwModule(leftRightMotionL);
        leftRightMotionModuleR = new GraphVeiwModule(leftRightMotionR);
        legStatusModuleL = new GraphVeiwModule(legStatusL);
        legStatusModuleR = new GraphVeiwModule(legStatusR);


//        groundReactionModuleL.setData(temp);
//        groundReactionModuleR.setData(temp2);
        propulsionModuleL.setData(temp);
        propulsionModuleR.setData(temp);
        stillnessModuleL.setData(temp);
        stillnessModuleR.setData(temp);
        leftRightMotionModuleL.setData(temp);
        leftRightMotionModuleR.setData(temp);
        legStatusModuleL.setData(temp);
        legStatusModuleR.setData(temp);


        test = new DatabaseJson();
        test.groundReactionModuleL = groundReactionModuleL;
        test.groundReactionModuleR = groundReactionModuleR;
        test.postData("http://192.168.20.63:80/json",null,getApplicationContext());


        //タイマーを新規生成
        Timer timer = new Timer();
        TimerTask timerTask = new display();
        timer.scheduleAtFixedRate(timerTask, 2000, 2000);


    }

    public class display extends TimerTask {
        private Handler handler;
        private boolean onece = false;

        public display() {
            handler = new Handler();
        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("json_main", String.valueOf(test.L_AI_FP_FZ));
                    Log.d("json_main", String.valueOf(test.R_AI_FP_FZ));

                    try{
                        //groundReactionModuleL.setData(test.L_AI_FP_FZ);
                        //groundReactionModuleR.setData(test.R_AI_FP_FZ);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //groundReactionModuleR.setData(temp);

                }
            });
        }

    }
}