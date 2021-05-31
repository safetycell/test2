package com.example.shokacshoes;
import android.util.Log;

import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class NumericalAnalysis {


    public Double calculatePopulationStandardDiviation(List<Double> data){

        //計算用変数
        SynchronizedSummaryStatistics stats = new SynchronizedSummaryStatistics();
        for (Double score : data){
            stats.addValue(score);
        }
        //母分散
        Double varp = stats.getPopulationVariance();
        //平方根
        Double stdevp = FastMath.sqrt(varp);

        //桁数を丸める
        // anser = Precision.round(stdevp, 6);
        //Log.d("calc", String.valueOf(stdevp));
        return stdevp;
    }

    public Double calculateAverage(List<Double> data){

        //計算用変数
        SynchronizedSummaryStatistics stats = new SynchronizedSummaryStatistics();
        for (Double score : data){
            stats.addValue(score);
        }

        Double average = stats.getMean();
        //Log.d("calc", String.valueOf(average));

        return average;

    }

}
