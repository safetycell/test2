package com.example.shokacshoes;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateFormat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class ShoesJson {

    //顧客ID
    String mailaddress;
    //パスワード
    String password;
    //ファイル名(日時)
    String filename;
    //計測開始時間
    String timeOfStartMeasurement;


    JSONArray L_Fx1;
    JSONArray L_Fy1;
    JSONArray L_Fz1;
    JSONArray L_Mx1;
    JSONArray L_My1;
    JSONArray L_Mz1;
    JSONArray L_Fx2;
    JSONArray L_Fy2;
    JSONArray L_Fz2;
    JSONArray L_Mx2;
    JSONArray L_My2;
    JSONArray L_Mz2;
    JSONArray L_Fx3;
    JSONArray L_Fy3;
    JSONArray L_Fz3;
    JSONArray L_Mx3;
    JSONArray L_My3;
    JSONArray L_Mz3;
    JSONArray L_AccelX;
    JSONArray L_AccelY;
    JSONArray L_AccelZ;
    JSONArray L_GyroX;
    JSONArray L_GyroY;
    JSONArray L_GyroZ;

    JSONArray R_Fx1;
    JSONArray R_Fy1;
    JSONArray R_Fz1;
    JSONArray R_Mx1;
    JSONArray R_My1;
    JSONArray R_Mz1;
    JSONArray R_Fx2;
    JSONArray R_Fy2;
    JSONArray R_Fz2;
    JSONArray R_Mx2;
    JSONArray R_My2;
    JSONArray R_Mz2;
    JSONArray R_Fx3;
    JSONArray R_Fy3;
    JSONArray R_Fz3;
    JSONArray R_Mx3;
    JSONArray R_My3;
    JSONArray R_Mz3;
    JSONArray R_AccelX;
    JSONArray R_AccelY;
    JSONArray R_AccelZ;
    JSONArray R_GyroX;
    JSONArray R_GyroY;
    JSONArray R_GyroZ;

    JSONArray L_CoGx;
    JSONArray L_CoGy;
    JSONArray R_CoGx;
    JSONArray R_CoGy;

    JSONObject jsonObject;

    //シューズID
    String shoes_ID = "0010001";
    //体調
    int Q_condition;
    //ストレス
    int Q_stress;

    //校正計数
    public double L_CFz1 = 0;
    public double L_CFz2 = 0;
    public double L_CFz3 = 0;
    public double R_CFz1 = 0;
    public double R_CFz2 = 0;
    public double R_CFz3 = 0;

    String url = "http://192.168.20.63:80/setdata";

    //String url = "http://192.168.1.3:80/post";



    public void SetShoes(Sensor6axis left,Sensor6axis right,CorrectionCoefficient coefficientv){
        jsonObject = new JSONObject();
        L_Fx1 = new JSONArray(left.sensor1.fx);
        L_Fy1 = new JSONArray(left.sensor1.fy);
        L_Fz1 = new JSONArray(left.sensor1.fz);
        L_Mx1 = new JSONArray(left.sensor1.mx);
        L_My1 = new JSONArray(left.sensor1.my);
        L_Mz1 = new JSONArray(left.sensor1.mz);

        L_Fx2 = new JSONArray(left.sensor2.fx);
        L_Fy2 = new JSONArray(left.sensor2.fy);
        L_Fz2 = new JSONArray(left.sensor2.fz);
        L_Mx2 = new JSONArray(left.sensor2.mx);
        L_My2 = new JSONArray(left.sensor2.my);
        L_Mz2 = new JSONArray(left.sensor2.mz);

        L_Fx3 = new JSONArray(left.sensor3.fx);
        L_Fy3 = new JSONArray(left.sensor3.fy);
        L_Fz3 = new JSONArray(left.sensor3.fz);
        L_Mx3 = new JSONArray(left.sensor3.mx);
        L_My3 = new JSONArray(left.sensor3.my);
        L_Mz3 = new JSONArray(left.sensor3.mz);

        L_AccelX = new JSONArray(left.accel.ax);
        L_AccelY = new JSONArray(left.accel.ay);
        L_AccelZ = new JSONArray(left.accel.az);
        L_GyroX = new JSONArray(left.accel.wx);
        L_GyroY = new JSONArray(left.accel.wy);
        L_GyroZ = new JSONArray(left.accel.wz);

        R_Fx1 = new JSONArray(right.sensor1.fx);
        R_Fy1 = new JSONArray(right.sensor1.fy);
        R_Fz1 = new JSONArray(right.sensor1.fz);
        R_Mx1 = new JSONArray(right.sensor1.mx);
        R_My1 = new JSONArray(right.sensor1.my);
        R_Mz1 = new JSONArray(right.sensor1.mz);

        R_Fx2 = new JSONArray(right.sensor2.fx);
        R_Fy2 = new JSONArray(right.sensor2.fy);
        R_Fz2 = new JSONArray(right.sensor2.fz);
        R_Mx2 = new JSONArray(right.sensor2.mx);
        R_My2 = new JSONArray(right.sensor2.my);
        R_Mz2 = new JSONArray(right.sensor2.mz);

        R_Fx3 = new JSONArray(right.sensor3.fx);
        R_Fy3 = new JSONArray(right.sensor3.fy);
        R_Fz3 = new JSONArray(right.sensor3.fz);
        R_Mx3 = new JSONArray(right.sensor3.mx);
        R_My3 = new JSONArray(right.sensor3.my);
        R_Mz3 = new JSONArray(right.sensor3.mz);

        R_AccelX = new JSONArray(right.accel.ax);
        R_AccelY = new JSONArray(right.accel.ay);
        R_AccelZ = new JSONArray(right.accel.az);
        R_GyroX = new JSONArray(right.accel.wx);
        R_GyroY = new JSONArray(right.accel.wy);
        R_GyroZ = new JSONArray(right.accel.wz);

        L_CoGx = new JSONArray(left.centerOfGravity.gx);
        L_CoGy = new JSONArray(left.centerOfGravity.gy);
        R_CoGx = new JSONArray(right.centerOfGravity.gx);
        R_CoGy = new JSONArray(right.centerOfGravity.gy);


        try {
            mailaddress = "1@gmail.com";
            password = "az";

            jsonObject.put("user_ID",mailaddress);
            jsonObject.put("password",password);


            //timeOfStartMeasurement = (String) DateFormat.format("yyyy-MM-dd kk:mm:ss:SSS", Calendar.getInstance());
            //filename = timeOfStartMeasurement;

            Calendar cl = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");
            timeOfStartMeasurement = sdf.format(cl.getTime());


            Log.d("millTime",timeOfStartMeasurement);
            jsonObject.put("file_name","1@gmail.com202105101315");
            jsonObject.put("Time",timeOfStartMeasurement);
            jsonObject.put("shoes_ID", shoes_ID);

            Q_condition = 1;
            Q_stress = 3;

            jsonObject.put("Q_condition", Q_condition);
            jsonObject.put("Q_stress", Q_stress);


            L_CFz1 = coefficientv.Left_AI_1;
            L_CFz2 = coefficientv.Left_AI_2;
            L_CFz3 = coefficientv.Left_AI_3;
            R_CFz1 = coefficientv.Right_AI_1;
            R_CFz2 = coefficientv.Right_AI_2;
            R_CFz3 = coefficientv.Right_AI_3;


            jsonObject.put("L_Fx1",L_Fx1);
            jsonObject.put("L_Fy1",L_Fy1);
            jsonObject.put("L_Fz1",L_Fz1);
            jsonObject.put("L_Mx1",L_Mx1);
            jsonObject.put("L_My1",L_My1);
            jsonObject.put("L_Mz1",L_Mz1);

            jsonObject.put("L_Fx2",L_Fx2);
            jsonObject.put("L_Fy2",L_Fy2);
            jsonObject.put("L_Fz2",L_Fz2);
            jsonObject.put("L_Mx2",L_Mx2);
            jsonObject.put("L_My2",L_My2);
            jsonObject.put("L_Mz2",L_Mz2);

            jsonObject.put("L_Fx3",L_Fx3);
            jsonObject.put("L_Fy3",L_Fy3);
            jsonObject.put("L_Fz3",L_Fz3);
            jsonObject.put("L_Mx3",L_Mx3);
            jsonObject.put("L_My3",L_My3);
            jsonObject.put("L_Mz3",L_Mz3);

            jsonObject.put("L_AccelX",L_AccelX);
            jsonObject.put("L_AccelY",L_AccelY);
            jsonObject.put("L_AccelZ",L_AccelZ);
            jsonObject.put("L_GyroX",L_GyroX);
            jsonObject.put("L_GyroY",L_GyroX);
            jsonObject.put("L_GyroZ",L_GyroX);


            jsonObject.put("R_Fx1",R_Fx1);
            jsonObject.put("R_Fy1",R_Fy1);
            jsonObject.put("R_Fz1",R_Fz1);
            jsonObject.put("R_Mx1",R_Mx1);
            jsonObject.put("R_My1",R_My1);
            jsonObject.put("R_Mz1",R_Mz1);

            jsonObject.put("R_Fx2",R_Fx2);
            jsonObject.put("R_Fy2",R_Fy2);
            jsonObject.put("R_Fz2",R_Fz2);
            jsonObject.put("R_Mx2",R_Mx2);
            jsonObject.put("R_My2",R_My2);
            jsonObject.put("R_Mz2",R_Mz2);

            jsonObject.put("R_Fx3",R_Fx3);
            jsonObject.put("R_Fy3",R_Fy3);
            jsonObject.put("R_Fz3",R_Fz3);
            jsonObject.put("R_Mx3",R_Mx3);
            jsonObject.put("R_My3",R_My3);
            jsonObject.put("R_Mz3",R_Mz3);


            jsonObject.put("R_AccelX",R_AccelX);
            jsonObject.put("R_AccelY",R_AccelY);
            jsonObject.put("R_AccelZ",R_AccelZ);
            jsonObject.put("R_GyroX",R_GyroX);
            jsonObject.put("R_GyroY",R_GyroY);
            jsonObject.put("R_GyroZ",R_GyroZ);


            jsonObject.put("L_CFz1",L_CFz1);
            jsonObject.put("L_CFz2",L_CFz2);
            jsonObject.put("L_CFz3",L_CFz3);
            jsonObject.put("R_CFz1",R_CFz1);
            jsonObject.put("R_CFz2",R_CFz2);
            jsonObject.put("R_CFz3",R_CFz3);

            jsonObject.put("L_CoGx",L_CoGx);
            jsonObject.put("L_CoGy",L_CoGy);
            jsonObject.put("R_CoGx",R_CoGx);
            jsonObject.put("R_CoGy",R_CoGy);

            jsonObject.put("L_Fx1C",coefficientv.Left_Offset_1_Fx);
            jsonObject.put("L_Fy1C",coefficientv.Left_Offset_1_Fy);
            jsonObject.put("L_Fz1C",coefficientv.Left_Offset_1_Fz);
            jsonObject.put("L_Mx1C",coefficientv.Left_Offset_1_Mx);
            jsonObject.put("L_My1C",coefficientv.Left_Offset_1_My);
            jsonObject.put("L_Mz1C",coefficientv.Left_Offset_1_Mz);
            jsonObject.put("R_Fx1C",coefficientv.Right_Offset_1_Fx);
            jsonObject.put("R_Fy1C",coefficientv.Right_Offset_1_Fy);
            jsonObject.put("R_Fz1C",coefficientv.Right_Offset_1_Fz);
            jsonObject.put("R_Mx1C",coefficientv.Right_Offset_1_Mx);
            jsonObject.put("R_My1C",coefficientv.Right_Offset_1_My);
            jsonObject.put("R_Mz1C",coefficientv.Right_Offset_1_Mz);

            jsonObject.put("L_Fx2C",coefficientv.Left_Offset_2_Fx);
            jsonObject.put("L_Fy2C",coefficientv.Left_Offset_2_Fy);
            jsonObject.put("L_Fz2C",coefficientv.Left_Offset_2_Fz);
            jsonObject.put("L_Mx2C",coefficientv.Left_Offset_2_Mx);
            jsonObject.put("L_My2C",coefficientv.Left_Offset_2_My);
            jsonObject.put("L_Mz2C",coefficientv.Left_Offset_2_Mz);
            jsonObject.put("R_Fx2C",coefficientv.Right_Offset_2_Fx);
            jsonObject.put("R_Fy2C",coefficientv.Right_Offset_2_Fy);
            jsonObject.put("R_Fz2C",coefficientv.Right_Offset_2_Fz);
            jsonObject.put("R_Mx2C",coefficientv.Right_Offset_2_Mx);
            jsonObject.put("R_My2C",coefficientv.Right_Offset_2_My);
            jsonObject.put("R_Mz2C",coefficientv.Right_Offset_2_Mz);

            jsonObject.put("L_Fx3C",coefficientv.Left_Offset_3_Fx);
            jsonObject.put("L_Fy3C",coefficientv.Left_Offset_3_Fy);
            jsonObject.put("L_Fz3C",coefficientv.Left_Offset_3_Fz);
            jsonObject.put("L_Mx3C",coefficientv.Left_Offset_3_Mx);
            jsonObject.put("L_My3C",coefficientv.Left_Offset_3_My);
            jsonObject.put("L_Mz3C",coefficientv.Left_Offset_3_Mz);
            jsonObject.put("R_Fx3C",coefficientv.Right_Offset_3_Fx);
            jsonObject.put("R_Fy3C",coefficientv.Right_Offset_3_Fy);
            jsonObject.put("R_Fz3C",coefficientv.Right_Offset_3_Fz);
            jsonObject.put("R_Mx3C",coefficientv.Right_Offset_3_Mx);
            jsonObject.put("R_My3C",coefficientv.Right_Offset_3_My);
            jsonObject.put("R_Mz3C",coefficientv.Right_Offset_3_Mz);


            jsonObject.put("L_CFz1",coefficientv.Left_AI_1);
            jsonObject.put("L_CFz2",coefficientv.Left_AI_2);
            jsonObject.put("L_CFz3",coefficientv.Left_AI_3);
            jsonObject.put("R_CFz1",coefficientv.Right_AI_1);
            jsonObject.put("R_CFz2",coefficientv.Right_AI_2);
            jsonObject.put("R_CFz3",coefficientv.Right_AI_3);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SendJSON(Context context){
        postData(url,jsonObject,context);
    }

    private void postData(String url, JSONObject data, Context context){
        /* タイムアウト 10000ミリ秒（10分）*/
        int custom_timeout_ms = 10 * 60 * 1000;
        DefaultRetryPolicy policy = new DefaultRetryPolicy(custom_timeout_ms,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);



        RequestQueue requstQueue = Volley.newRequestQueue(context);


        JsonObjectRequest jsonobj = new JsonObjectRequest(Request.Method.POST, url,data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ){
            //here I want to post data to sever
        };
        jsonobj.setRetryPolicy(policy);

        requstQueue.add(jsonobj);

    }


}
