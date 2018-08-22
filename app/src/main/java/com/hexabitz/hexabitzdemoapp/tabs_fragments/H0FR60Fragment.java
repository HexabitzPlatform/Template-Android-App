package com.hexabitz.hexabitzdemoapp.tabs_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hexabitz.hexabitzdemoapp.ModulesActivity;
import com.hexabitz.hexabitzdemoapp.R;

import java.util.Timer;
import java.util.TimerTask;


public class H0FR60Fragment extends Fragment {

    Button toggleBtn;
    Button relayBtn;
    EditText module_id,time_out;
    boolean isToggled = false;
    boolean bRelayMode = false;
    Timer relay_t = new Timer();

    public H0FR60Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_h0_fr60, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toggleBtn = (Button) getActivity().findViewById(R.id.toggle_btn);
        relayBtn = (Button) getActivity().findViewById(R.id.relay_on_btn);
        module_id = (EditText) getActivity().findViewById(R.id.module_id_et);
        time_out= (EditText) getActivity().findViewById(R.id.txtTimeout);

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String module_id_str = module_id.getText().toString();
                if(isToggled){
                    ((ModulesActivity)getActivity()).sendData(0x03,0x00, 0x05DD, null,module_id_str);
                    toggleBtn.setText("Toggle OFF");
                    toggleBtn.setBackgroundColor(getResources().getColor(R.color.off));
                    isToggled = false;
                }else{
                    ((ModulesActivity)getActivity()).sendData(0x03,0x00, 0x05DE, null,module_id_str);
                    toggleBtn.setText("Toggle ON");
                    toggleBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isToggled = true;
                }

            }
        });

        relayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String module_id_str = module_id.getText().toString();
                int nTimeout = 0;
                try
                {
                    nTimeout = Integer.parseInt( time_out.getText().toString());
                }
                catch (Exception ex)
                {

                }
                byte[] data= {
                        (byte) (nTimeout >> 24),
                        (byte) ((nTimeout & 0xFFFFFF) >> 16),
                        (byte) ((nTimeout & 0xFFFF) >> 8),
                        (byte) (nTimeout & 0xFF)
                };
                ((ModulesActivity)getActivity()).sendData(0x03,0x00, 0x05DC, data,module_id_str);
//            SendData("on " + ((SeekBar) findViewById(R.id.seekBarTimeOut)).getProgress());
                bRelayMode = true;
                relayBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                relayBtn.setEnabled(false);
                relayBtn.setText("ON");

                relay_t.schedule(new TimerTask() {

                    @Override
                    public void run() {

                        relayBtn.post(new Runnable() {
                            @Override
                            public void run() {
                                relayBtn.setText("OFF");
                                relayBtn.setBackgroundColor(getResources().getColor(R.color.off));
                                relayBtn.setEnabled(true);}
                        });

                    }
                },nTimeout);

            }
        });

    }
}
