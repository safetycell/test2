package com.example.shokacshoes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SensorAccelerator {

    public List<Double> ax ;
    public List<Double> ay ;
    public List<Double> az ;
    public List<Double> wx ;
    public List<Double> wy ;
    public List<Double> wz ;
    int saveIndex;
    int BTindex;



    private String outputMessage;
    public SensorAccelerator(){
        ax = new ArrayList<Double>();
        ay = new ArrayList<Double>();
        az = new ArrayList<Double>();
        wx = new ArrayList<Double>();
        wy = new ArrayList<Double>();
        wz = new ArrayList<Double>();
        saveIndex = 0;
        BTindex = 0;
    }
    public void setAccelerator(String s){
/*
        int temp = Math.abs(BTindex - saveIndex) % 254;

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

        */
        try{
            String[] AcceleratorData = s.split(",", 7);
            //Log.d("serialbt", AcceleratorData[0]);
            String tempb = AcceleratorData[0].substring(0, 2);
            BTindex = Integer.parseInt(tempb,16);


            int temp = Math.abs(BTindex - saveIndex) % 254;

            for (int i = 0; i < (temp - 1); i++) {
                ax.add(ax.get(saveIndex));
                ay.add(ay.get(saveIndex));
                az.add(az.get(saveIndex));
                wx.add(wx.get(saveIndex));
                wy.add(wy.get(saveIndex));
                wz.add(wz.get(saveIndex));
            }




            ax.add(Double.parseDouble(AcceleratorData[1]));
            ay.add(Double.parseDouble(AcceleratorData[2]));
            az.add(Double.parseDouble(AcceleratorData[3]));
            wx.add(Double.parseDouble(AcceleratorData[4]));
            wy.add(Double.parseDouble(AcceleratorData[5]));
            wz.add(Double.parseDouble(AcceleratorData[6]));

            saveIndex = BTindex;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

}