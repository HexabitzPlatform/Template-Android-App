package com.hexabitz.hexabitzdemoapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity {

    ArrayList<String> devicesNameList;
    ArrayList<String> devicesAddressList;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<BluetoothDevice> availableDevices;
    BluetoothDevice bdDevice;
    BluetoothClass bdClass;
    BluetoothAdapter mBluetoothAdapter = null;
    ListView listview;
    CustomeArrayAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        listview = (ListView) findViewById(R.id.paired_devices_list);

        devicesNameList = new ArrayList<String>();
        devicesAddressList = new ArrayList<String>();
        availableDevices = new ArrayList<BluetoothDevice>();


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onStart() {
        super.onStart();
        // Quick permission check
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        devicesAddressList.clear();
        devicesNameList.clear();
        availableDevices.clear();
        bluetoothScanning();
    }

    void bluetoothScanning(){

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getApplicationContext().registerReceiver(mReceiver, filter);
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
            Log.e("Log", "Bluetooth is Enabled");
        }
        mBluetoothAdapter.startDiscovery();

        adapter = new CustomeArrayAdapter(this,
                -1, devicesNameList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent data = new Intent(PairedDevicesActivity.this,ModulesActivity.class);
//                data.putExtra("address", Uri.parse(devicesAddressList.get(position)).toString());
//                startActivity(data);
                bdDevice = availableDevices.get(position);
                Boolean isBonded = false;
                try {
                    isBonded = bdDevice.createBond();
                    if(isBonded)
                    {
                        Intent data = new Intent(PairedDevicesActivity.this,ModulesActivity.class);
                        data.putExtra("address", Uri.parse(devicesAddressList.get(position)).toString());
                        startActivity(data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }//connect(bdDevice);
                Log.e("Log", "The bond is created: "+isBonded);
            }
        });

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a new device
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                availableDevices.add(device);
                Log.e("Device Name: ", "device " + device.getName());
                Log.e("deviceHardwareAddress ", "hard" + device.getAddress());
                devicesNameList.add(device.getName() + '\n' + device.getAddress());
                devicesAddressList.add(device.getAddress());
                adapter.notifyDataSetChanged();

            }
//            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//                Intent data = new Intent(PairedDevicesActivity.this,ModulesActivity.class);
//                data.putExtra("address", Uri.parse(device.getAddress()).toString());
//                startActivity(data);
//            }


        }
    };



    private class CustomeArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        List<String> values = new ArrayList<String>();

        public CustomeArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            values = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.listitem, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.title);
            textView.setText(values.get(position));

            return rowView;
        }

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
