package com.example.sensoruse;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button vibrateBtn, blueOnBtn, blueOffBtn, wifiOnBtn, wifiOffBtn;
    private SeekBar seekBar;
    private TextView status;
    private BluetoothAdapter mBluetoothAdapter;
    private WifiManager wifiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    vibrateBtn = findViewById(R.id.VibrateBtn);
    blueOnBtn = findViewById(R.id.blueOnBtn);
    blueOffBtn = findViewById(R.id.blueOffBtn);
    wifiOnBtn = findViewById(R.id.wifiOnBtn);
    wifiOffBtn = findViewById(R.id.wifiOffBtn);
    seekBar = findViewById(R.id.seekBar);
    status = findViewById(R.id.status);


    // SeekBar 상태 변화
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(progress < 10){
                progress = 10;
                seekBar.setProgress(progress);
            }
            status.setText("밝기 수준 : " + progress);

            //화면 밝기 조절
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.screenBrightness = (float) progress / 100;
            getWindow().setAttributes(params);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    });

    }

    //진동 울리기 버튼
    public void onVibrateClicked(View view) {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    //블루투스 ON 버튼, 화면 전환
    public void onBluetoothONClicked(View view) {
        Intent intentm = new Intent(getApplicationContext(), BluetoothActivity.class);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable(); //강제 활성화
        }
        startActivity(intentm);

    }

    //블루투스 OFF 버튼
    public void onBluetoothOFFClicked(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Bluetooth OFF", Toast.LENGTH_SHORT).show();
    }

    //와이파이 ON 버튼
    public void onWifiOnClicked(View view) {
        Intent intentw = new Intent(getApplicationContext(), WifiActivity.class);
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }
        startActivity(intentw);
    }

    //와이파이 OFF 버튼
    public void onWifiOffClicked(View view) {
        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
        }
        Toast.makeText(getApplicationContext(), "Wifi OFF", Toast.LENGTH_SHORT).show();
    }
}