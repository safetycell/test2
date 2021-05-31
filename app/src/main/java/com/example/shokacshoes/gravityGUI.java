package com.example.shokacshoes;

import android.widget.ImageView;

public class gravityGUI{
    private ImageView gravityImage;
    private float sensor1Fz;
    private float sensor2Fz;
    private float sensor3Fz;
    private float sensor1X;
    private float sensor1Y;
    private float sensor2X;
    private float sensor2Y;
    private float sensor3X;
    private float sensor3Y;
    private float centerX;
    private float centerY;
    private float threshold;
    private float i;
    private final int LEFT_LEG = 1;
    private final int RIGHT_LEG = 2;

    public gravityGUI(ImageView id,int leg){
        gravityImage = id;
        if(leg == RIGHT_LEG) {
            sensor1X = 61;
            sensor1Y = 140;
            sensor2X = 125;
            sensor2Y = 165;
            sensor3X = 95;
            sensor3Y = 326;
            //5は円の直径。つまりオフセット
            centerX = 89+5;
            centerY = 206+5;
        }else{
            sensor1X = 180-61;
            sensor1Y = 140;
            sensor2X = 180-125;
            sensor2Y = 165;
            sensor3X = 180-95;
            sensor3Y = 326;
            centerX = 180-89-5;
            centerY = 206+5;

        }

        threshold = 30;
    }

    private int convertionPx(float DIP){

        final float GESTURE_THRESHOLD_DIP = 16.0f;

        // Convert the dips to pixels
        final float scale = gravityImage.getContext().getResources().getDisplayMetrics().density;
        return (int) (DIP * scale + 0.5f);
    }

    public void setSensor1Fz(int power){
        sensor1Fz = power;
        //Log.d("gravity", "1 : " + String.valueOf(power));





        //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
        gravityImage.setTranslationX(convertionPx(getGravityX()));
        gravityImage.setTranslationY(convertionPx(getGravityY()));
        //Log.d("gravity", String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+" , "+  String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

    }

    public void setSensor2Fz(int power){
        sensor2Fz = power;
        //Log.d("gravity", "2 : " + String.valueOf(power));
        //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
        gravityImage.setTranslationX(convertionPx(getGravityX()));
        gravityImage.setTranslationY(convertionPx(getGravityY()));
        //Log.d("gravity",  String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ " , "+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

    }
    public void setSensor3Fz(int power){
        sensor3Fz = power;
        //Log.d("gravity", "3 : " + String.valueOf(power));
        //Log.d("gravity", String.valueOf(sensor1Fz) +" , " + String.valueOf(sensor2Fz) +" , " +String.valueOf(sensor3Fz) +" , "+ String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));
        gravityImage.setTranslationX(convertionPx(getGravityX()));
        gravityImage.setTranslationY(convertionPx(getGravityY()));
        //Log.d("gravity",  String.valueOf(getGravityX()) +" , " + String.valueOf(getGravityY())+" , "+ String.valueOf(gravityImage.getTranslationX())+" , " + String.valueOf(gravityImage.getTranslationY()));

    }
    private float getGravityX(){
        float s1 = sensor1Fz < threshold ? 0 : sensor1Fz;
        float s2 = sensor2Fz < threshold ? 0 : sensor2Fz;
        float s3 = sensor3Fz < threshold ? 0 : sensor3Fz;
        if(s1 == 0 && s2 == 0 && s3 == 0){
            return 0;
        }else {
            return (((centerX - sensor1X) * s1 + (centerX - sensor2X) * s2 + (centerX - sensor3X) * s3) / (s1 + s2 + s3) * -1);
        }
    }
    private float getGravityY(){
        float s1 = sensor1Fz < threshold ? 0 : sensor1Fz;
        float s2 = sensor2Fz < threshold ? 0 : sensor2Fz;
        float s3 = sensor3Fz < threshold ? 0 : sensor3Fz;

        if(s1 == 0 && s2 == 0 && s3 == 0){
            return 0;
        }else{
            return ((centerY-sensor1Y) * s1 + (centerY-sensor2Y) * s2 + (centerY-sensor3Y) * s3) / (s1 + s2 + s3)*-1;
        }
    }

}
