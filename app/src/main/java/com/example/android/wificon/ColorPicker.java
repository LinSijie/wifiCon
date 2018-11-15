package com.example.android.wificon;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.wificon.utilities.NetworkUtils;
import com.skydoves.colorpickerview.AlphaTileView;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import java.io.IOException;
import java.util.Arrays;

public class ColorPicker extends AppCompatActivity {

    private static final String TAG = "ColorPicker";
    private int[] ARGB;
    private boolean isLedOn;
    private ColorPickerView colorPickerView;
    private TextView show_device_IP;
    private Switch mLedStatus;
    String hostIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);
        Intent mIntent = getIntent();
        hostIP = mIntent.getStringExtra("hostIP");
        Log.i(TAG, "onCreate: hostIP = " + hostIP);

        show_device_IP = (TextView)this.findViewById(R.id.tv_ip_address);
        show_device_IP.setText(hostIP);

        mLedStatus = (Switch)this.findViewById(R.id.switch_light);
        mLedStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLedOn = isChecked == true ? true : false;
                ledParamBuilder();
            }
        });
        colorPickerView = findViewById(R.id.colorPickerView);
        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                setLayoutColor(envelope);
            }
        });
        // attach brightnessSlideBar
        final BrightnessSlideBar brightnessSlideBar = findViewById(R.id.brightnessSlide);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);
    }

    /**
     * set layout color & textView html code
     *
     * @param envelope ColorEnvelope by ColorEnvelopeListener
     */
    private void setLayoutColor(ColorEnvelope envelope) {
        int[] newARGB = colorPickerView.getColorARGB(envelope.getColor());
        if(Arrays.equals(ARGB, newARGB) == true ){
            return;
        }
        ARGB = newARGB;
        TextView showColorValue = findViewById(R.id.tv_color_value);
        showColorValue.setText("R" + ARGB[1] + " " +
                         "G" + ARGB[2] + " " +
                         "B" + ARGB[3]);

        AlphaTileView alphaTileView = findViewById(R.id.alphaTileView);
        alphaTileView.setPaintColor(envelope.getColor());
        ledParamBuilder();
    }

    /**
     * set LED params
     *
     */
    private void ledParamBuilder() {
        String [] params = {
                hostIP,
                Boolean.toString(isLedOn),
                Integer.toString(ARGB[1]),
                Integer.toString(ARGB[2]),
                Integer.toString(ARGB[3])
        };
        new setColorTask().execute(params);
    }


    public class setColorTask extends AsyncTask<String[], Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String[]... strings) {

            /* If there's no zip code, there's nothing to look up. */
            if (strings.length == 0) {
                return null;
            }

            String[] params = strings[0];

            try {
                NetworkUtils.sendPostRequest(params);
                return "OK";
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

}
