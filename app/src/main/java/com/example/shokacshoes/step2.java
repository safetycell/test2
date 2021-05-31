package com.example.shokacshoes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class step2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step2);



        Button to_step_3_button = (Button)findViewById(R.id.to_step3);
        to_step_3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), step3.class);
                startActivity(intent);
            }
        });



        Button to_step_resister = (Button)findViewById(R.id.to_resister);
        to_step_resister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), resister.class);
                startActivity(intent);
            }
        });




        //Intent intent = new Intent(getApplication(), );
        //startActivity(intent);
    }
}