package com.hexabitz.hexabitzdemoapp.tabs_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.hexabitz.hexabitzdemoapp.ModulesActivity;
import com.hexabitz.hexabitzdemoapp.R;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerView;

import java.util.Timer;
import java.util.TimerTask;


public class H01R00Fragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    ColorPickerView colorPickerView;
    protected boolean isConnected = false;
    EditText module_id;
    boolean is_locked = false,isLedOn = false;
    Button rbg_btn,off_on_btn;
    SeekBar r_seekBar,g_seekBar,b_seekBar,opacity_seekBar;
    Timer t = new Timer();

    public H01R00Fragment() {
    }


    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {


        rbg_btn.setBackgroundColor(
                getIntFromColor(
                        opacity_seekBar.getProgress(),
                        r_seekBar.getProgress(),
                        g_seekBar.getProgress(),
                        b_seekBar.getProgress()
                )
        );
        if (!is_locked) {
            byte[] data = {
                    0x1,
                    (byte)r_seekBar.getProgress(),
                    (byte)g_seekBar.getProgress(),
                    (byte)b_seekBar.getProgress(),
                    (byte)opacity_seekBar.getProgress(),
            };

            ((ModulesActivity)getActivity()).sendData(0x02, 0, 0x67, data,module_id.getText().toString());
            is_locked = true;
            t.schedule(new TimerTask() {
//
                @Override
                public void run() {
                    is_locked=false;
                }
            },100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rbg_btn = (Button) getActivity().findViewById(R.id.led_btn);
        off_on_btn = (Button) getActivity().findViewById(R.id.on_off_btn);
        r_seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_R);
        g_seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_G);
        b_seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_G);
        opacity_seekBar = (SeekBar) getActivity().findViewById(R.id.seek_bar_opacity);

        module_id = (EditText) getActivity().findViewById(R.id.module_id_r00_et);
        r_seekBar.setOnSeekBarChangeListener(this);
        g_seekBar.setOnSeekBarChangeListener(this);
        b_seekBar.setOnSeekBarChangeListener(this);
        opacity_seekBar.setOnSeekBarChangeListener(this);

        r_seekBar.setProgress(128);
        g_seekBar.setProgress(128);
        b_seekBar.setProgress(128);
        opacity_seekBar.setProgress(99);



        colorPickerView = (ColorPickerView)getActivity().findViewById(R.id.colorPickerView);

        colorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                int rgb [] = colorEnvelope.getColorRGB();
                rbg_btn.setBackgroundColor(
                        getIntFromColor(
                                opacity_seekBar.getProgress() * 255 / 100,
                                rgb[0],
                                rgb[1],
                                rgb[2]
                        )
                );
                r_seekBar.setProgress(rgb[0]);
                g_seekBar.setProgress(rgb[1]);
                b_seekBar.setProgress(rgb[2]);
            }
        });

        off_on_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moduleId = module_id.getText().toString();
                if (isLedOn == false)
                {
                    byte opacity = (byte) (opacity_seekBar.getProgress());
                    byte[] data = {opacity};
                    ((ModulesActivity)getActivity()).sendData(0x02,0, 0x64, data, moduleId);
                    isLedOn = true;
                    off_on_btn.setText("Led ON");
                    rbg_btn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                }
                else
                {
                    ((ModulesActivity)getActivity()).sendData(0x02,0, 0x65, null, moduleId);
                    isLedOn = false;
                    off_on_btn.setText("Led OFF");
                    rbg_btn.setBackgroundColor(getResources().getColor(R.color.off));
                }
            }
        });
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    public int getIntFromColor(int Alpha, int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return ((Alpha << 24) & 0xFF000000) | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_h01_r00, container, false);
    }

}
