package com.example.shokacshoes;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CenterOfGravity {

    public List<Double> gx ;
    public List<Double> gy ;

    //右足左足にあるセンサの中心からの位置
    double s1x = -29;
    double s1y = 85;
    double s2x = 29;
    double s2y = 63;
    double s3x = 4;
    double s3y = -85;
    //しきい値の設定
    double thresholed = 50;

    private String outputMessage;
    public CenterOfGravity(){
        gx = new ArrayList<Double>();
        gy = new ArrayList<Double>();
    }
    public void SetGravity(String s,axis6 s1,axis6 s2,axis6 s3){
        try{

            if(s == "left") {
                for (int i = 0; i < s3.fz.size(); i++) {
                    if ((s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)) > thresholed) {
                        gx.add((s1x * s1.fz.get(i) + s2x * s2.fz.get(i) + s3x * s3.fz.get(i)) / (s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)));
                        gy.add((s1y * s1.fz.get(i) + s2y * s2.fz.get(i) + s3y * s3.fz.get(i)) / (s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)));
                    } else {
                        gx.add(0.0);
                        gy.add(0.0);

                    }
                }
            }
            //右左でミラーになるのでs1とs2のポジションが入れ変わる
            else if(s == "right"){
                for (int i = 0; i < s3.fz.size(); i++) {
                    if ((s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)) > thresholed) {
                        gx.add((s2x * s1.fz.get(i) + s1x * s2.fz.get(i) + s3x * s3.fz.get(i)) / (s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)));
                        gy.add((s2y * s1.fz.get(i) + s1y * s2.fz.get(i) + s3y * s3.fz.get(i)) / (s1.fz.get(i) + s2.fz.get(i) + s3.fz.get(i)));
                    } else {
                        gx.add(0.0);
                        gy.add(0.0);

                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
