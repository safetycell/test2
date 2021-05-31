package com.example.shokacshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class step1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.step1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //NumericalAnalysis n = new NumericalAnalysis();
        //n.calculatePopulationStandardDiviation(Arrays.asList(1.0,2.0,3.0,4.0,5.0));
        //n.calculateAverage(Arrays.asList(1.0,2.0,3.0,4.0,5.0));


        Button to_step_2_button = (Button)findViewById(R.id.to_step2);
        to_step_2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplication(), step2.class);

                //Intent intent = new Intent(getApplication(), step2.class);
                Intent intent = new Intent(getApplication(), WarmingUp.class);
                //Intent intent = new Intent(getApplication(), ViewGraph.class);

                startActivity(intent);
            }
        });




        //Intent intent = new Intent(getApplication(), );
        //startActivity(intent);
    }
}