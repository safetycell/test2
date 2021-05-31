package com.example.shokacshoes;

import android.graphics.Color;
import android.os.Build;
import android.widget.TextView;

import androidx.annotation.RequiresApi;



public class sliceGUI{
    private TextView sliceBarPositive, sliceBarNegative;

    public sliceGUI(TextView Up_or_Left ,TextView Down_or_Right){
        sliceBarPositive = Up_or_Left;
        sliceBarNegative = Down_or_Right;
        sliceBarPositive.setTextColor(Color.rgb(0,0,255));
        sliceBarNegative.setTextColor(Color.rgb(0,0,255));
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setSlicePower(double power){
        if(power>100){
            power = 100;
        }
        if(power<-100){
            power = -100;
        }
        if(power > 50) {
            sliceBarNegative.setTextColor(Color.rgb(255, (int)(255 - 255 * (power-50) / 50), 0));
        }else if(power > 0) {
            sliceBarNegative.setTextColor(Color.rgb((int)(255 * power / 50), (int)(255 * power / 50), (int)(255 - 255 * power / 50)));
        }else if(power < -50){
            sliceBarPositive.setTextColor(Color.rgb(255, (int)(255 - 255 * (-power-50) / 50), 0));
        }else if(power < 0){
            sliceBarPositive.setTextColor(Color.rgb((int)(255 * -power / 50), (int)(255 * -power / 50), (int)(255 - 255 * -power / 50)));
        }else{
            sliceBarPositive.setTextColor(Color.rgb(0,0,255));
            sliceBarNegative.setTextColor(Color.rgb(0,0,255));
        }

    }
}
