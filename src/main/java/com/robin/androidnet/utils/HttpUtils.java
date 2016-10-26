package com.robin.androidnet.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/10/17.
 */
public class HttpUtils {
    public static byte[] getWebData(URL url) {
        HttpURLConnection connection = null;
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod("GET");
            connection.connect();
            int respondCode = connection.getResponseCode();
            bos = new ByteArrayOutputStream();
            if (respondCode == HttpURLConnection.HTTP_OK) {
                is = connection.getInputStream();
                byte[] result = new byte[1024];
                int cnt;
                while (((cnt = is.read(result)) != -1)) {
                    bos.write(result, 0, cnt);
                    bos.flush();
                }
                return bos.toByteArray();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (bos != null) {
                    bos.close();
                }
                if (connection != null) {
                    connection.disconnect();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}
