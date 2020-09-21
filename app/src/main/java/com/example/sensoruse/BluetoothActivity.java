package com.example.sensoruse;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity {

    final static int BLUETOOTH_REQUEST_CODE = 100;

    BluetoothAdapter mBluetoothAdapter;
    private ListView bluetoothlist, pairedlist;
    private Button searchBtn;

    SimpleAdapter adapterDevice, adapterPaired;
    List<Map<String, String>> dataPaired, dataDevice;
    List<BluetoothDevice> bluetoothDevices;
    int selectDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        Intent intent = getIntent();

        bluetoothlist = findViewById(R.id.bluetoothList);
        pairedlist = findViewById(R.id.pairedList);
        searchBtn = findViewById(R.id.searchBtn);


        dataDevice = new ArrayList<>();
        adapterDevice = new SimpleAdapter(this, dataDevice, android.R.layout.simple_list_item_2,
                new String[]{"name", "address"}, new int[]{android.R.id.text1, android.R.id.text2});
        bluetoothlist.setAdapter(adapterDevice);

        // 페어링 된 디바이스 목록 화면에 뿌리기
        dataPaired = new ArrayList<>();
        adapterPaired = new SimpleAdapter(this, dataPaired, android.R.layout.simple_list_item_2,
                new String[]{"name", "address"}, new int[]{android.R.id.text1, android.R.id.text2});
        pairedlist.setAdapter(adapterPaired);

        //검색된 블루투스 디바이스 데이터
        bluetoothDevices = new ArrayList<>();
        //선택한 디바이스 없음
        selectDevice = -1;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "블루투스 지원하지 않는 단말기", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //BluetoothAdapter.ACTION_DISCOVERY_STARTED : 블루투스 검색 시작
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND); //BluetoothDevice.ACTION_FOUND : 블루투스 디바이스 찾음
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //BluetoothAdapter.ACTION_DISCOVERY_FINISHED : 블루투스 검색 종료
        searchFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBluetoothSearchReceiver, searchFilter);


        bluetoothlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothDevice device = bluetoothDevices.get(position);
                try {
                    //선택한 디바이스 페어링 요청
                    Method method = device.getClass().getMethod("createBond", (Class[]) null);
                    method.invoke(device, (Object[]) null);
                    selectDevice = position;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //검색 버튼 눌렀을 때
    public void onSearchClicked(View view) {
        //검색 누르면 버튼 비활성화
        searchBtn.setEnabled(false);
        //블루투스 검색 중이면, 취소

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        //블루투스 검색 시작
        mBluetoothAdapter.startDiscovery();
    }


    //블루투스 검색 목록 받아오기
    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    dataDevice.clear();
                    bluetoothDevices.clear();
                    Toast.makeText(BluetoothActivity.this, "블루투스 검색 시작", Toast.LENGTH_SHORT).show();
                    break;

                //블루투스 디바이스 찾았을 때
                case BluetoothDevice.ACTION_FOUND:
                    //검색한 블루투스 디바이스의 객체 구하기
                    //Log.v("kjw_listdevice", String.valueOf(dataDevice));
                    //Log.v("kjw_bluetoothDevices", String.valueOf(bluetoothDevices));
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.v("kjw_listdevice2", device.getName() + "");
                    //블루투스 디바이스 이름과 주소 저장
                    Map map = new HashMap();
                    map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                    map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                    dataDevice.add(map);

                    //리스트 목록갱신
                    adapterDevice.notifyDataSetChanged();

                    //블루투스 디바이스 저장
                    bluetoothDevices.add(device);

                    break;

                //검색 종료
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(BluetoothActivity.this, "블루투스 검색 종료", Toast.LENGTH_SHORT).show();
                    searchBtn.setEnabled(true);
                    break;

                //블루투스 디바이스 페어링 상태 변화
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice paired = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (paired.getBondState() == BluetoothDevice.BOND_BONDED) {
                        //데이터 저장
                        Map map2 = new HashMap();
                        map2.put("name", paired.getName()); //device.getName() : 블루투스 디바이스의 이름
                        map2.put("address", paired.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                        dataPaired.add(map2);
                        //리스트 목록갱신
                        adapterPaired.notifyDataSetChanged();

                        //검색된 목록
                        if (selectDevice != -1) {
                            bluetoothDevices.remove(selectDevice);

                            dataDevice.remove(selectDevice);
                            adapterDevice.notifyDataSetChanged();
                            selectDevice = -1;
                        }
                    }
                    break;
            }
        }
    };

    //이미 페어링된 목록 가져오기
    public void GetListPairedDevice() {
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();

        dataPaired.clear();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                //데이터 저장
                Map map = new HashMap();
                map.put("name", device.getName()); //device.getName() : 블루투스 디바이스의 이름
                map.put("address", device.getAddress()); //device.getAddress() : 블루투스 디바이스의 MAC 주소
                dataPaired.add(map);
            }
        }
        //리스트 목록갱신
        adapterPaired.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BLUETOOTH_REQUEST_CODE:
                //블루투스 활성화 승인
                if (resultCode == Activity.RESULT_OK) {
                    GetListPairedDevice();
                }
                //블루투스 활성화 거절
                else {
                    Toast.makeText(this, "블루투스를 활성화해야 합니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mBluetoothSearchReceiver);
        super.onDestroy();
    }

}