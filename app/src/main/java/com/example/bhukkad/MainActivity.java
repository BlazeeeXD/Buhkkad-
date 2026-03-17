package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Link your Java variables to the Buttons in your XML layout
        Button btnPage1 = findViewById(R.id.staff_dashboard);
      /*  Button btnPage2 = findViewById(R.id.btnPage2);
        Button btnPage3 = findViewById(R.id.btnPage3);
        Button btnPage4 = findViewById(R.id.btnPage4); */

        // 2. Set up the click listeners for each button

        // Button 1 -> FirstActivity
        btnPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StaffDashboardActivity.class);
                startActivity(intent);
            }
        });

       /* // Button 2 -> SecondActivity
        btnPage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        // Button 3 -> ThirdActivity
        btnPage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                startActivity(intent);
            }
        });

        // Button 4 -> FourthActivity
        btnPage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FourthActivity.class);
                startActivity(intent);
            }
        });*/
    }
}