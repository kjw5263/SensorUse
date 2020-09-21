package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btn;
    TextView state_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.Button1);
        state_tv = findViewById(R.id.state);
        saveState.save_activity="";
        saveState.save_activity="onCreate\n";
        Log.v("kjw_onCreate", saveState.save_activity);

    }

    @Override
    protected void onStart() {
        super.onStart();
        saveState.save_activity += "onStart\n";
        Log.v("kjw_onStart", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        saveState.save_activity += "onRestart\n";
        Log.v("kjw_onRestart", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        saveState.save_activity += "onResume\n";
        Log.v("kjw_onResume", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState.save_activity = "onPause\n";
        Log.v("kjw_onPause", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState.save_activity = "onStop\n";
        Log.v("kjw_onStop", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveState.save_activity = "onDestroy\n";
        Log.v("kjw_onDestroy", saveState.save_activity);
        state_tv.setText(saveState.save_activity);
    }

    public void onButtonClicked(View view) {
        Intent intent = new Intent(this, SubActivity.class);
        startActivity(intent);
        finish();
    }
}