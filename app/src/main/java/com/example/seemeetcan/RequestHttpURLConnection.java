package com.example.seemeetcan;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RequestHttpURLConnection {
    private URL url;

    public RequestHttpURLConnection() throws MalformedURLException {
        url = new URL("http://193.123.245.244:5000");
    }

    public RequestHttpURLConnection(String string) throws MalformedURLException {
        url = new URL("http://193.123.245.244:5000/" + string);
    }

    public String request(String data) {
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.connect();

            OutputStream os = connection.getOutputStream();
            os.write(data.getBytes("UTF-8"));
            os.flush();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] byteBuffer = new byte[1024];
            byte[] byteData = null;
            int nLength = 0;
            while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                baos.write(byteBuffer, 0, nLength);
            }
            byteData = baos.toByteArray();
            String str = new String(byteData);
            result = str;

        } catch (IOException e) {

        }
        return result;
    }

}
