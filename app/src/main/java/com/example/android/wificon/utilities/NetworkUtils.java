package com.example.android.wificon.utilities;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.ContentValues.TAG;

public final class NetworkUtils {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void sendPostRequest(String[] params) throws IOException {

        String charset = "UTF-8";
        String ip_addr = params[0];
        String red = params[1];
        String green = params[2];
        String blue = params[3];

        String query = String.format("Red=%s&Green=%s&Blue=%s&",
                URLEncoder.encode(red, charset),
                URLEncoder.encode(green, charset),
                URLEncoder.encode(blue, charset));
        Log.d(TAG, "query: " + query);

        HttpURLConnection httpConnection =  (HttpURLConnection) new URL("http://" + ip_addr).openConnection();
        httpConnection.setRequestMethod("POST");
        httpConnection.setDoOutput(true); // Triggers POST.
        httpConnection.setDoInput(true);
        httpConnection.setInstanceFollowRedirects(false);
        httpConnection.setRequestProperty("Accept-Charset", charset);
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        httpConnection.connect();

        try (OutputStream output = httpConnection.getOutputStream()) {
            output.write(query.getBytes(charset));
            //Log.d(TAG, "output stream:" + output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream response = httpConnection.getInputStream();
        httpConnection.disconnect();
    }
}
