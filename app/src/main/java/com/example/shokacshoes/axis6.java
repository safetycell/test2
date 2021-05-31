package com.example.shokacshoes;


import android.graphics.Color;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;


public class axis6{

    public List<Float> fx ;
    public List<Float> fy ;
    public List<Float> fz ;
    public List<Float> mx ;
    public List<Float> my ;
    public List<Float> mz ;
    private int saveIndex;
    private int sensorNo;


    private RotationGUI rotationGUI;
    private PushGUI pushGUI;
    private sliceGUI tateGUI;
    private sliceGUI yokoGUI;


    public axis6(){
        fx = new ArrayList<Float>();
        fy = new ArrayList<Float>();
        fz = new ArrayList<Float>();
        mx = new ArrayList<Float>();
        my = new ArrayList<Float>();
        mz = new ArrayList<Float>();
        saveIndex = 0;

    }

    public void setRotaionGUI(TextView input, int number) {
        rotationGUI = new RotationGUI(input);
        sensorNo = number;
    }


    public void setPushGUI(TextView input) {
        pushGUI = new PushGUI(input);
    }

    public void setSlideGUI(TextView up,TextView down,TextView left,TextView right) {
        tateGUI = new sliceGUI(up,down);
        yokoGUI = new sliceGUI(left,right);

    }

    public void SetAxis6(float fxx, float fyy, float fzz, float mxx, float myy, float mzz, int BTindex){
        int temp = Math.abs(BTindex - saveIndex) % 254;

        if(sensorNo == 1 || sensorNo == 2) {
            rotationGUI.setRotateX(-mxx / 5);
            rotationGUI.setRotateY(-myy / 5);

            yokoGUI.setSlicePower(-fxx / 5);
            tateGUI.setSlicePower(-fyy / 5);
        }else if (sensorNo == 3){
            rotationGUI.setRotateX(mxx / 5);
            rotationGUI.setRotateY(myy / 5);

            yokoGUI.setSlicePower(fxx / 5);
            tateGUI.setSlicePower(fyy / 5);
        }

        pushGUI.setSlicePower(fzz / 5);

        for (int i = 0; i < (temp - 1); i++) {
            fx.add(fx.get(saveIndex));
            fy.add(fy.get(saveIndex));
            fz.add(fz.get(saveIndex));
            mx.add(mx.get(saveIndex));
            my.add(my.get(saveIndex));
            mz.add(mz.get(saveIndex));
        }

        fx.add(fxx);
        fy.add(fyy);
        fz.add(fzz);
        mx.add(mxx);
        my.add(myy);
        mz.add(mzz);

        saveIndex = BTindex;
    }
    public double getAverageFx(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += fx.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public double getAverageFy(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += fy.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public double getAverageFz(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += fz.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public double getAverageMx(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += mx.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public double getAverageMy(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += my.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public double getAverageMz(int start,int end){
        try{
            double sum = 0;
            for(int i = start*50;i<end*50;i++){
                sum += mz.get(i);
            }
            return sum / (double)((end - start)*50);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}