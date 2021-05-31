package com.example.shokacshoes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class step3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step3);



        Button to_step_4_button = (Button)findViewById(R.id.to_step4);
        to_step_4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), step4.class);
                Log.d("message","step4へ移動");
                startActivity(intent);
            }
        });




        //Intent intent = new Intent(getApplication(), );
        //startActivity(intent);
    }
}