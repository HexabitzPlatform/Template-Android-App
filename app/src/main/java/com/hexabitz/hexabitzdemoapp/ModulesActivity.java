package com.hexabitz.hexabitzdemoapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.hexabitz.hexabitzdemoapp.tabs_fragments.BtnsAndSwitchesFragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.CLIFragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.H01R00Fragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.H0FR60Fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class ModulesActivity extends AppCompatActivity {
    private byte[] mmBuffer;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    BLEHandler bleHandler = new BLEHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new H01R00Fragment(), "H01R00");
        adapter.addFragment(new H0FR60Fragment(), "H0FR60");
        adapter.addFragment(new BtnsAndSwitchesFragment(), "Buttons & Switches");
        adapter.addFragment(new CLIFragment(), "CLI");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void connectToDevice(String address) {
        /* 00001101-0000-1000-8000-00805f9b34fb Serial*/
        /* 00000004-0000-1000-8000-00805f9b34fb  TCP*/
        UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
        BluetoothSocket tmp = null;
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            Log.e("DEBUG", "Socket's create() method failed", e);
            return;
        }
        mmDevice = device;
        mmSocket = tmp;

        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            Log.e("DEBUG", "Could not connect the client socket", connectException);
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("DEBUG", "Could not close the client socket", closeException);
            }
            return;
        }

        try {
            mmOutStream = mmSocket.getOutputStream();
            mmInStream = mmSocket.getInputStream();
        } catch (IOException e) {
            Log.e("DEBUG", "Error occurred when creating output stream", e);

        }

        /// Start handler to listen and receive data
        bleHandler.execute();

    }

    public void sendData(int dest, int src, int code, byte[] arrData, String module_id)
    {
        try {
            int size=6;
            if (arrData != null)
                size = 6 + arrData.length;
            int dest_id = Integer.parseInt(module_id);
            byte[] data = new byte[size];
            data[0]= (byte) (size-1);//0x06;
            data[1]= (byte) dest_id;//dest;//0x02;
            data[2]=0x0;
            data[3]= (byte) ((code>>8) & 0x00FF);//0x0;
            data[4]=(byte) ((code) & 0x00FF);//0x64;
            if (arrData != null){
                for (int i = 0; i < arrData.length; i++) {
                    data[i+5]=arrData[i];
                }
            }

            data[size-1]=0x75;
            mmOutStream.write(data);
        } catch (IOException e) {
            Log.e("DEBUG", "Error occurred when sending cmd", e);
        }
    }

    public void turn_off_on(View view){

    }
}
