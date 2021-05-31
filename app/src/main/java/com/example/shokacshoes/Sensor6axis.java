package com.example.shokacshoes;

import android.widget.TextView;

import org.json.JSONObject;

public class Sensor6axis {
    private int offset;
    private int ESP32Index;
    public axis6 sensor1 ;
    public axis6 sensor2 ;
    public axis6 sensor3 ;
    public SensorAccelerator accel;
    private String s;
    //private Accel accel = new Accel();

    String fxs,fys,fzs,mxs,mys,mzs;
    public float fx;
    public float fy;
    public float fz;
    public float mx;
    public float my;
    public float mz;

    double offset_Fx_1 = 0;
    double offset_Fy_1 = 0;
    double offset_Fz_1 = 0;
    double offset_Mx_1 = 0;
    double offset_My_1 = 0;
    double offset_Mz_1 = 0;
    double offset_Fx_2 = 0;
    double offset_Fy_2 = 0;
    double offset_Fz_2 = 0;
    double offset_Mx_2 = 0;
    double offset_My_2 = 0;
    double offset_Mz_2 = 0;
    double offset_Fx_3 = 0;
    double offset_Fy_3 = 0;
    double offset_Fz_3 = 0;
    double offset_Mx_3 = 0;
    double offset_My_3 = 0;
    double offset_Mz_3 = 0;
    CenterOfGravity centerOfGravity;

    int A,B;

    private JSONObject sendToServerData;

    public void SetGravity(){
        centerOfGravity.SetGravity(s,sensor1,sensor2,sensor3);
    }

    public void setRotationGUI(TextView s1,TextView s2,TextView s3){
        sensor1.setRotaionGUI(s1,1);
        sensor2.setRotaionGUI(s2,2);
        sensor3.setRotaionGUI(s3,3);
    }

    public void setSlideGUI(TextView s1_Up,TextView s1_Down,TextView s1_Left,TextView s1_Right,
                            TextView s2_Up,TextView s2_Down,TextView s2_Left,TextView s2_Right,
                            TextView s3_Up,TextView s3_Down,TextView s3_Left,TextView s3_Right){
        sensor1.setSlideGUI(s1_Up,s1_Down,s1_Left,s1_Right);
        sensor2.setSlideGUI(s2_Up,s2_Down,s2_Left,s2_Right);
        sensor3.setSlideGUI(s3_Up,s3_Down,s3_Left,s3_Right);


    }

    public void setGravityGUI(){

    }

    public void setPushGUI(TextView s1,TextView s2,TextView s3){
        sensor1.setPushGUI(s1);
        sensor2.setPushGUI(s2);
        sensor3.setPushGUI(s3);
    }

    public Sensor6axis(String leg){
        sensor1 = new axis6();
        sensor2 = new axis6();
        sensor3 = new axis6();
        accel = new SensorAccelerator();
        centerOfGravity = new CenterOfGravity();
        offset = 2;
        ESP32Index = 0;
        A = 0;
        B = 0;
        s = leg;
    }

    public void ResetData(){
        sensor1 = new axis6();
        sensor2 = new axis6();
        sensor3 = new axis6();
        accel = new SensorAccelerator();
        centerOfGravity = new CenterOfGravity();
        offset = 2;
        ESP32Index = 0;
        A = 0;
        B = 0;

    }




    public void Set6axis(String s){
        //Log.d("serialBT",s.substring(8,12));
        //Log.d("serialBT",String.valueOf(Integer.parseInt(s.substring(8,12), 16)));

        try {

                if (s.indexOf("010e") == 2 || s.indexOf("020e") == 2 || s.indexOf("030e") == 2) {

                try {
                    ESP32Index = Integer.parseInt(s.substring(0, 2), 16);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                fxs = s.substring(offset + 4, offset + 8);
                fys = s.substring(offset + 8, offset + 12);
                fzs = s.substring(offset + 12, offset + 16);
                mxs = s.substring(offset + 16, offset + 20);
                mys = s.substring(offset + 20, offset + 24);
                mzs = s.substring(offset + 24, offset + 28);
                try {
                    fx = Integer.parseInt(fxs, 16) - 2048;
                    fy = Integer.parseInt(fys, 16) - 2048;
                    fz = Integer.parseInt(fzs, 16) - 2048;
                    mx = Integer.parseInt(mxs, 16) - 2048;
                    my = Integer.parseInt(mys, 16) - 2048;
                    mz = Integer.parseInt(mzs, 16) - 2048;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (s.indexOf("010e") == 2) {
                    fx -= offset_Fx_1;
                    fy -= offset_Fy_1;
                    fz -= offset_Fz_1;
                    mx -= offset_Mx_1;
                    my -= offset_My_1;
                    mz -= offset_Mz_1;

                    sensor1.SetAxis6(fx, fy, fz, mx, my, mz, ESP32Index);
                } else if (s.indexOf("020e") == 2) {
                    fx -= offset_Fx_2;
                    fy -= offset_Fy_2;
                    fz -= offset_Fz_2;
                    mx -= offset_Mx_2;
                    my -= offset_My_2;
                    mz -= offset_Mz_2;
                    sensor2.SetAxis6(fx, fy, fz, mx, my, mz, ESP32Index);
                } else if (s.indexOf("030e") == 2) {

                    fx -= offset_Fx_3;
                    fy -= offset_Fy_3;
                    fz -= offset_Fz_3;
                    mx -= offset_Mx_3;
                    my -= offset_My_3;
                    mz -= offset_Mz_3;
                    sensor3.SetAxis6(fx, fy, fz, mx, my, mz, ESP32Index);

                }
            }else if(s.indexOf("04") == 2 || s.indexOf("0404") == 0) {
                try {
                    accel.setAccelerator(s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String[] AcceleratorData = s.split(",", 7);
                //Log.d("serialbt", AcceleratorData[0]);



            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        //Log.d("serialBT",String.valueOf(fx));
    }




}
