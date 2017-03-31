package com.example.zhuangqf.myverityapp.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.zhuangqf.myverityapp.App;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

/**
 * Created by zhuangqf on 2/8/17.
 */
public class MyInternetTask extends AsyncTask <Object,Void,String[]>{

    private Context context;
    private App app;

    public MyInternetTask(Context context,App app){
        this.context = context;
        this.app = app;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected String[] doInBackground(Object ... params) {

        SSLContext sslContext = (SSLContext)params[0];
        final String host = (String) params[1];
        final String message = "{ \"data\":" + params[2] + "}";

        Log.e(this.getClass().getName(),host);
        Log.e(this.getClass().getName(),message);

        HttpURLConnection connection = null;
        InputStreamReader in = null;

        try{
            URL url = new URL("https://"+host);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return hostname.equals("112.74.22.182");
                }
            });


            sendMessage(urlConnection, message);

            String produceId = urlConnection.getHeaderField("x-produceId");
            Log.e(this.getClass().getName(),"x-produceId"+produceId);

            String content = downloadFile(urlConnection);

            String[] result = new String[2];
            result[0] = content;
            result[1] = produceId;
            return result;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(in!=null)
                    in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                if(connection!=null)
                    connection.disconnect();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }

    private void sendMessage(HttpsURLConnection connection,String message) throws Exception{

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(message);

        outputStream.flush();
        outputStream.close();

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String downloadFile(HttpsURLConnection connection)  throws Exception{

        StringBuilder result = new StringBuilder();
        try(InputStreamReader in = new InputStreamReader(connection.getInputStream())) {
            char[] buffer = new char[1024];
            int n = 0;
            while((n= in.read(buffer,0,buffer.length))>0){
                result.append(buffer, 0, n);
            }
            return result.toString();
        }
    }

    @Override
    protected void onPostExecute(String[] result) {
        if((result == null) || (result.length != 2)){
            Toast.makeText(context,"激活失败，点击屏幕继续",Toast.LENGTH_LONG).show();
            return;
        }
        String content = result[0];
        String produceId = result[1];
        if(content==null||"".equals(content)||content.length()==0){
            Toast.makeText(context,"激活失败，点击屏幕继续",Toast.LENGTH_LONG).show();
            return;
        }
        FileService fileService = new FileService(context);
        try {
            fileService.writeFile(content,app.fileName);
            SharedPreferences sharedPreferences = context.getSharedPreferences(app.preference_file_key, Context.MODE_PRIVATE);
            sharedPreferences.edit().putString(app.produce_id_key, produceId).apply();
            app.isDownload = true;
            Toast.makeText(context,"激活成功，点击屏幕继续",Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
