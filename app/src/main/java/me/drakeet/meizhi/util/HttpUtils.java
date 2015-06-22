package me.drakeet.meizhi.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by drak11t on 8/16/14.
 */
public class HttpUtils {

    private URL url = null;

    public String download(String urlStr) {
        StringBuffer sb = new StringBuffer();
        String line = null;
        BufferedReader buffer = null;
        try {
            url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            buffer = new BufferedReader(
                    new InputStreamReader(
                            urlConn.getInputStream()
                    )
            );
            while ((line = buffer.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
