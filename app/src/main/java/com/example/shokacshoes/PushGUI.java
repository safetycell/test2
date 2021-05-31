package com.example.shokacshoes;

import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

public class PushGUI {
    private TextView center;

    public PushGUI(TextView input){
        center = input;
        center.setTextColor(Color.rgb(0,0,255));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSlicePower(double power){
        if(power>100){
            power = 100;
        }
        if(power<0){
            power = 0;
        }

        if(power > 50) {
            center.setTextColor(Color.rgb(255, (int)(255 - 255 * (power-50) / 50), 0));
        }else if(power > 0) {
            center.setTextColor(Color.rgb((int)(255 * power / 50), (int)(255 * power / 50), (int)(255 - 255 * power / 50)));
        }else{
            center.setTextColor(Color.rgb(0,0, 255));
        }

    }
}
