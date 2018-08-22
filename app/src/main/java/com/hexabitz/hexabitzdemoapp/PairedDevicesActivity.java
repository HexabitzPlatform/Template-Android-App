package com.hexabitz.hexabitzdemoapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        ListView listview = (ListView) findViewById(R.id.paired_devices_list);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        devicesNameList = new ArrayList<String>();
        devicesAddressList = new ArrayList<String>();
        for(BluetoothDevice bt : pairedDevices) {
            devicesNameList.add(bt.getName());
            devicesAddressList.add(bt.getAddress());
        }

        final CustomeArrayAdapter adapter = new CustomeArrayAdapter(this,
                -1, devicesNameList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent data = new Intent(PairedDevicesActivity.this,ModulesActivity.class);
                data.putExtra("address",Uri.parse(devicesAddressList.get(position)).toString());
                startActivity(data);
            }
        });

    }



    private class CustomeArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        ArrayList<String> values = new ArrayList<String>();

        public CustomeArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            this.context = context;
            for (int i = 0; i < objects.size(); ++i) {
                values.add(objects.get(i));
            }
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
