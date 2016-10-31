package io.eidukas.fivethirtyeight;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class ApiRequest {
    private String url;

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;

        //While there are still letters to be read from the reader
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private String readJsonFromUrl(String url, int timeout) throws IOException{

        URL site = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) site.openConnection();
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        if(conn.getResponseCode() != 200){
            Log.d("HTTP ERROR CODE", String.valueOf(conn.getResponseCode()));
        }

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream(), Charset.forName("UTF-8"))){
            BufferedReader rd = new BufferedReader(reader);
            return readAll(rd);
        }
    }

    public String getResponse(int timeout)throws IOException{
        return readJsonFromUrl(url, timeout);
    }

    public ApiRequest(String url){
        this.url = url;
    }
}
