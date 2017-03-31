package com.example.zhuangqf.myverityapp.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.zhuangqf.myverityapp.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhuangqf on 3/17/17.
 */
public class MyHttpTask extends AsyncTask<Object,Void,String> {

    private Context context;

    public MyHttpTask(Context context){
        this.context = context;
    }

    @Override
    protected String doInBackground(Object... params) {
        String host = (String) params[0];
        String message = "{ \"data\":" + params[1] + "}";

        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        StringBuilder result  = new StringBuilder();

        try {
            url = new URL(host);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(message);

            outputStream.flush();
            outputStream.close();

            in = new InputStreamReader(connection.getInputStream());
            Log.e(this.getClass().getName(),"encoding:"+in.getEncoding());
            char[] buffer = new char[1024];
            int n = 0;
            while ((n = in.read(buffer, 0, buffer.length))>0){
                result.append(buffer,0,n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String temp) {
        Log.e(this.getClass().getName(),"收到服务器的数据："+temp);
        FileService fileService = new FileService(context);
        try {
//            fileService.writeFile(temp, "temp");
//            Log.e(this.getClass().getName(), "write to file temp");
//            Toast.makeText(context,"write file succeed",Toast.LENGTH_LONG).show();
//            String encryptTemp = fileService.readFile("temp");
//            String fileTemp = CryptoService.decrypt(encryptTemp, Build.SERIAL);
//            fileTemp = CryptoService.decrypt(fileTemp,"123456");
//            String data = fileService.getStringFromRawId(R.raw.app);
//            if(data.equals(fileTemp)) {
//                Toast.makeText(context,"equal",Toast.LENGTH_LONG).show();
//            }else {
//                Toast.makeText(context,"not equal",Toast.LENGTH_LONG).show();
//            }
//            Log.e(this.getClass().getName(), "temp.length:" + temp.length());
//            Log.e(this.getClass().getName(), "data.length:" + data.length());
//            Log.e(this.getClass().getName(), "fileTemp.length:" + fileTemp.length());
//            for(int i=0;i<fileTemp.length();i++){
//                if(fileTemp.indexOf(i)!=data.indexOf(i)){
//                    Log.e(this.getClass().getName(),"not equal in "+i+" that temp = "+fileTemp.indexOf(i)+" and data = "+data.indexOf(i));
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
