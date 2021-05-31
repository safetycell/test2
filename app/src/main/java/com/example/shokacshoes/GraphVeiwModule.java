package com.example.shokacshoes;

import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class GraphVeiwModule {
    LineChart chart;

    public GraphVeiwModule(LineChart mChart){
        chart = mChart;
        setSetting(mChart);
    }

    private void setSetting(LineChart mChart) {
        if(mChart != null) {
            // Grid背景色
            mChart.setDrawGridBackground(true);

            // no description text
            mChart.getDescription().setEnabled(true);

            // Grid縦軸を破線
            XAxis xAxis = mChart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            YAxis leftAxis = mChart.getAxisLeft();
            // Y軸最大最小設定
            leftAxis.setAxisMaximum(500f);
            leftAxis.setAxisMinimum(0f);
            // Grid横軸を破線
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(true);

            // 右側の目盛り
            mChart.getAxisRight().setEnabled(false);

            //入れるとアニメーションになる
            //mChart.animateX(2500);
            //mChart.invalidate();

            // dont forget to refresh the drawing
            // mChart.invalidate();

            //Descriptionを非表示に
            Description des = mChart.getDescription();
            des.setEnabled(false);

            //系列名の非表示
            mChart.getLegend().setEnabled(false);
        }
    }
    public void setData(List<Float> temp) {


        ArrayList<Entry> values = new ArrayList<>();

//        for (int i = 0; i < data.length; i++) {
//            values.add(new Entry(i, data[i], null, null));
//        }

        for (int i = 0; i < temp.size(); i++) {
            values.add(new Entry(i, temp.get(i), null, null));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet");

            set1.setDrawIcons(false);
            set1.setColor(Color.BLACK);

            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);


            set1.setDrawCircles(false);
            //set1.setCircleRadius(1f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            //中の色塗り
            //set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            //set1.setFillColor(Color.BLUE);

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData lineData = new LineData(dataSets);





            // set data
            chart.setData(lineData);
            chart.notifyDataSetChanged();
            chart.invalidate();
        }
    }
}
