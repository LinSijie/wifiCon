package com.example.android.wificon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.wificon.utilities.NetworkUtils;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView mIpInstruction;
    private EditText mIpAddress;
    private TextView mColorInstruction;
    private EditText mRedValue;
    private EditText mGreenValue;
    private EditText mBlueValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIpInstruction = (TextView)this.findViewById(R.id.tv_ip_instruction);
        mIpAddress = (EditText)this.findViewById(R.id.et_ip_address);
        mColorInstruction = (TextView)this.findViewById(R.id.tv_color_instruction);
        mRedValue = (EditText)this.findViewById(R.id.et_red_value);
        mGreenValue = (EditText)this.findViewById(R.id.et_green_value);
        mBlueValue = (EditText)this.findViewById(R.id.et_blue_value);
        setLedColor();
    }

    private void setLedColor() {
        String [] params = {
                mIpAddress.getText().toString(),
                mRedValue.getText().toString(),
                mGreenValue.getText().toString(),
                mBlueValue.getText().toString()
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.submit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemSelectedId = item.getItemId();
        if(itemSelectedId == R.id.action_submit){
            setLedColor();
            Context context =MainActivity.this;
            String textToShow = "Submit clicked";
            Toast.makeText(context,textToShow,Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
