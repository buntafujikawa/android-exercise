package com.example.buntafujikawa.rssfeed.Common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bunta.fujikawa on 2017/12/31.
 */

public class HTTPDataHandler {
    static String stream = "";

    public HTTPDataHandler() {
    }

    public String GetHTTPDataHandler(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return stream;
            }

            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line = "";

            while ((line = r.readLine()) != null) {
                sb.append(line);
                stream = sb.toString();
            }
            urlConnection.disconnect();

        } catch (Exception ex) {
            return null;
        }
        return stream;
    }
}
