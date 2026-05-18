package com.example.bhukkad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StaffScreen4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen4);

        Button btnReady = findViewById(R.id.btnReady);

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffScreen4Activity.this, StaffScreen5Activity.class);
                startActivity(intent);
            }
        });
    }
}