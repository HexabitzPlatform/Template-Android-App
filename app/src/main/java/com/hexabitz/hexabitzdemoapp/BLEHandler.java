package com.hexabitz.hexabitzdemoapp;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by Soubhi on 6/24/18.
 */


public class BLEHandler extends AsyncTask {
    Handler mHandler = new Handler();
    private byte[] mmBuffer;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private InputStream mmInStream;
    private OutputStream mmOutStream;


    @Override
    protected Object doInBackground(Object[] objects) {
        return null;
    }


    public void read() {
        mmBuffer = new byte[1024];
        Arrays.fill( mmBuffer, (byte) 0 );
        int numBytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                numBytes = mmInStream.read(mmBuffer);
                mmBuffer[numBytes] = 0;

                // Send the obtained bytes to the UI activity.
            } catch (IOException e) {
                Log.d("DEBUG", "Input stream was disconnected", e);
                break;
            }
        }
    }

}
