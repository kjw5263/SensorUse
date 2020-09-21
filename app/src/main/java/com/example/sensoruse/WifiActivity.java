package com.example.sensoruse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiEnterpriseConfig;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {
    private TextView wifiname_tv;
    //private Button resetBtn;
    private ListView wifilist;
    private ArrayAdapter adapter;
    private ArrayList<String> arrywifi = new ArrayList<>();
    private WifiManager wifiManager;
    private List<ScanResult> scanDatas;
    IntentFilter intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {

                wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                scanDatas = wifiManager.getScanResults();
                //Log.v("kjw_2222", scanDatas.toString()+"");
                //Log.v("kjw_2222arry", arrywifi.toString()+"");
                for(int i =0 ; i<scanDatas.size(); i++){
                    arrywifi.add(scanDatas.get(i).SSID);
                }
                Log.v("kjw_wifiList", arrywifi.toString());
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        Intent intentw = getIntent();

        wifiname_tv = findViewById(R.id.wifiname_tv);
        wifilist = findViewById(R.id.wifi_lv);

        //인텐트에 상수 값
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(receiver, intentFilter);
        wifiManager.startScan();

        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, arrywifi);
        wifilist.setAdapter(adapter);

        //와이파이 리스트 선택 이벤트
        wifilist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("index", position);
                intent.putExtra("wifiname", arrywifi.get(position));




                // 리스트 하나 눌렀을 때
                if( scanDatas.get(position).SSID == arrywifi.get(position)){

                   // WifiConfiguration conf = new WifiConfiguration();
                   // conf.SSID = "\"" + scanDatas.get(position).SSID + "\"";
                   // conf.wepKeys[position] = "\"" + scanDatas.get(position).
                   // conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);  //open Wifi

                    //다이얼로그 팝업창 띄우기
                    AlertDialog.Builder builder = new AlertDialog.Builder(WifiActivity.this);
                    builder.setTitle("Wifi Setting");
                    builder.setMessage(scanDatas.get(position).SSID + "\n비밀번호 입력");

                    //비밀번호 입력받는 editText
                    final EditText passwd = new EditText(WifiActivity.this);
                    builder.setView(passwd);

                    //확인 버튼 이벤트
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    WifiConfiguration wifiConfig = new WifiConfiguration();
                                    wifiConfig.SSID = String.format("\"%s\"", scanDatas.get(position).SSID);
                                    wifiConfig.preSharedKey = String.format("\"%s\"", passwd.getText());

                                    Log.d("aaaaaaaaaa","SSID >>>>>>>>> " + String.format("\"%s\"", scanDatas.get(position).SSID));
                                    Log.d("aaaaaaaaaa","preSharedKey >>>>>>>>> " + String.format("\"%s\"", passwd.getText()));

                                    //wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    int netId = wifiManager.addNetwork(wifiConfig);
                                    wifiManager.disconnect();
                                    wifiManager.enableNetwork(netId, true);
                                    wifiManager.reconnect();

                                }
                            });

                    //취소버튼 이벤트
                    builder.setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"취소",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();
                }

            }

        });

    }
}