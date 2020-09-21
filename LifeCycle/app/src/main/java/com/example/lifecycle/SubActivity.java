package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SubActivity extends AppCompatActivity {
    TextView result_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        result_tv = findViewById(R.id.result_tv);
    }


    @Override
    protected void onResume() {
        super.onResume();
        String Result = saveState.save_activity;
        result_tv.setText(Result);
    }
}