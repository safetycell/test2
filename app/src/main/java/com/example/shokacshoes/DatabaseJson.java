package com.example.shokacshoes;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import login.ui.login.LoginActivity;

public class DatabaseJson {

    JSONObject jsonObject;


    String url = "https://jsonplaceholder.typicode.com/todos/";


    //AI結果
    List<Float> L_AI_FP_FX = new ArrayList<>();
    List<Float> L_AI_FP_FY = new ArrayList<>();
    List<Float> L_AI_FP_FZ = new ArrayList<>();
    List<Float> R_AI_FP_FX = new ArrayList<>();
    List<Float> R_AI_FP_FY = new ArrayList<>();
    List<Float> R_AI_FP_FZ = new ArrayList<>();


    public GraphVeiwModule groundReactionModuleL;
    public GraphVeiwModule groundReactionModuleR;
    public GraphVeiwModule propulsionModuleL;
    public GraphVeiwModule propulsionModuleR;
    public GraphVeiwModule stillnessModuleR;
    public GraphVeiwModule stillnessModuleL;
    public GraphVeiwModule leftRightMotionModuleL;
    public GraphVeiwModule leftRightMotionModuleR;
    public GraphVeiwModule legStatusModuleL;
    public GraphVeiwModule legStatusModuleR;


    public void postData(String url, JSONObject data, Context context){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                        //Log.d("json","受信");
                        //Log.d("json",response);

                        L_AI_FP_FX = new ArrayList<>();
                        L_AI_FP_FY = new ArrayList<>();
                        L_AI_FP_FZ = new ArrayList<>();
                        R_AI_FP_FX = new ArrayList<>();
                        R_AI_FP_FY = new ArrayList<>();
                        R_AI_FP_FZ = new ArrayList<>();

                        if(response != ""){
                            JSONArray obj = null;
                            try {
                                obj = new JSONArray(response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            try {
                                for(int i=0;i<obj.length();i++){
                                    JSONObject jresponse = obj.getJSONObject(i);

                                    L_AI_FP_FX.add((float) jresponse.getDouble("L_AI_FP_FX"));
                                    L_AI_FP_FY.add((float) jresponse.getDouble("L_AI_FP_FY"));
                                    L_AI_FP_FZ.add((float) jresponse.getDouble("L_AI_FP_FZ"));
                                    R_AI_FP_FX.add((float) jresponse.getDouble("R_AI_FP_FX"));
                                    R_AI_FP_FY.add((float) jresponse.getDouble("R_AI_FP_FY"));
                                    R_AI_FP_FZ.add((float) jresponse.getDouble("R_AI_FP_FZ"));

                                }
                                groundReactionModuleL.setData(L_AI_FP_FZ);
                                groundReactionModuleR.setData(R_AI_FP_FZ);
                                //Log.d("json", String.valueOf(L_AI_FP_FZ));
                                //Log.d("json", String.valueOf(R_AI_FP_FZ));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("json","受信エラー");

                    }

                }){


        };
        requestQueue.add(stringRequest);

    }


}
