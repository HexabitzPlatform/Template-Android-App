package com.hexabitz.hexabitzdemoapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paired_devices);

        ListView listview = (ListView) findViewById(R.id.paired_devices_list);


        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for(BluetoothDevice bt : pairedDevices)
            s.add(bt.getName());

        final CustomeArrayAdapter adapter = new CustomeArrayAdapter(this,
                -1, s);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                startActivity(new Intent(PairedDevicesActivity.this,ModulesActivity.class));
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
