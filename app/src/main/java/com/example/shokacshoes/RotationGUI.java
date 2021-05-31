package com.example.shokacshoes;

import android.graphics.Color;
import android.widget.TextView;

public class RotationGUI {
    private TextView rotatingBody;
    private float rotateX;
    private float rotateY;


    public RotationGUI(TextView input){
        rotatingBody = input;
    }
    public void setRotateX(double i){
        rotateX = (float) i;
        rotatingBody.setRotationX(-rotateX);
        setColor();
    }
    public void setRotateY(double i){
        rotateY = (float) i;
        rotatingBody.setRotationY(-rotateY);
        setColor();
    }
    public void setRotateX(float i){
        rotateX = i;
        rotatingBody.setRotationX(-rotateX);
        setColor();
    }
    public void setRotateY(float i){
        rotateY = i;
        rotatingBody.setRotationY(-rotateY);
        setColor();
    }
    private void setColor(){
        rotatingBody.setTextColor(Color.rgb((int)5, (int)(240-(Math.abs(rotateX)+Math.abs(rotateY))/2*1.6), (int)0));
    }


}
