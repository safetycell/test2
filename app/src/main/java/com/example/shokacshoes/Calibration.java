package com.example.shokacshoes;

import android.util.Log;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;

import java.util.List;

public class Calibration {
    double weight;
    Sensor6axis sensor;
    public RealMatrix Cefficients;
    double[][] shoesCoff;
    List<Float> floatShoesData1;
    List<Float> floatShoesData2;
    List<Float> floatShoesData3;
    double[] floatShoesDataSetOffset1, floatShoesDataSetOffset2, floatShoesDataSetOffset3;

    double[][] floatShoesDataSetOffsetArray = new double[3][];

    public Calibration(double m_weight,Sensor6axis m_sensor){
        weight = m_weight;
        sensor = m_sensor;
    }

    public void getShoesValue(ShoesOffset offset,int start,int end){

        try {
            //1秒で50 3秒で150
            floatShoesData1 = sensor.sensor1.fz.subList(start * 50, end * 50);
            floatShoesData2 = sensor.sensor2.fz.subList(start * 50, end * 50);
            floatShoesData3 = sensor.sensor3.fz.subList(start * 50, end * 50);

            floatShoesDataSetOffset1 = new double[floatShoesData1.size()];
            for (int i = 0; i < floatShoesData1.size(); i++)
                floatShoesDataSetOffset1[i] = floatShoesData1.get(i) - offset.Offset_1_Fz;
            floatShoesDataSetOffset2 = new double[floatShoesData2.size()];
            for (int i = 0; i < floatShoesData2.size(); i++)
                floatShoesDataSetOffset2[i] = floatShoesData2.get(i) - offset.Offset_2_Fz;
            floatShoesDataSetOffset3 = new double[floatShoesData3.size()];
            for (int i = 0; i < floatShoesData3.size(); i++)
                floatShoesDataSetOffset3[i] = floatShoesData3.get(i) - offset.Offset_3_Fz;


            floatShoesDataSetOffsetArray[0] = floatShoesDataSetOffset1;
            floatShoesDataSetOffsetArray[1] = floatShoesDataSetOffset2;
            floatShoesDataSetOffsetArray[2] = floatShoesDataSetOffset3;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("MatricCalc","足りない");
        }

    }

    public double[][] getWeight(int start,int end){
        double[][] buff1 = new double[1][(end - start )*50];
        for(int i = 0; i < (end - start )*50; i++) buff1[0][i] = weight;
        return buff1;
    }

    public void Calc(ShoesOffset offset,int start,int end){
        try {
            //個別補正値を求める
            getShoesValue(offset,start, end);

            RealMatrix weight_array = MatrixUtils.createRealMatrix(getWeight(start, end));
            RealMatrix shoesValue = MatrixUtils.createRealMatrix(floatShoesDataSetOffsetArray);

            //Log.d("MatricCalc",shoesValue.);
            //weight = weight.transpose();

            RealMatrix M1 = weight_array.multiply(shoesValue.transpose());
            RealMatrix M2 = shoesValue.multiply(shoesValue.transpose());
            Cefficients = M1.multiply(MatrixUtils.blockInverse(M2, 0));

            RealMatrixFormat form = new RealMatrixFormat("Matrix\n", "", "", "\n", "", ", ");

//            Log.d("MatricCalc", form.format(weight_array));
//            Log.d("MatricCalc", form.format(shoesValue));
//            Log.d("MatricCalc", form.format(M1));
//            Log.d("MatricCalc", form.format(M2));
            Log.d("MatricCalc", form.format(Cefficients));
        } catch (NullArgumentException e) {
            e.printStackTrace();
        } catch (DimensionMismatchException e) {
            e.printStackTrace();
        } catch (NoDataException e) {
            e.printStackTrace();
        }

        //Log.d("MatricCalc",form.format(weight.multiply(shoesValue)));


    }

}
