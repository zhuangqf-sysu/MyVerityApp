package com.example.zhuangqf.myverityapp.service;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import com.example.zhuangqf.myverityapp.R;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

/**
 * Created by zhuangqf on 3/5/17.
 */
public class FileService {

    private final String TAG = this.getClass().getName();

    private Context context;

    public FileService(Context context){
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void writeFile(String content,String fileName) throws Exception{
        try(
                FileOutputStream fout = context.openFileOutput(fileName,Context.MODE_PRIVATE)
        ) {
            fout.write(content.getBytes());
        }
    }

    public boolean isFile(String fileName) {
        String[] fileList = context.fileList();
        for(String file:fileList){
            Log.e(this.getClass().getName(),"file:"+file);
            if(fileName.equals(file)) return true;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public byte[] readFile(String fileName) throws Exception {
        try(
                InputStream inputStream = context.openFileInput(fileName);
        ){
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public byte[] getByteArrayFromRawId(int id) throws Exception {

        try(InputStream inputStream = context.getResources().openRawResource(id)){
            byte[] bytes = new byte[inputStream.available()];
            int n = inputStream.read(bytes);
            if(n!=bytes.length){
                Log.e(TAG,"n!=bytes.length");
            }
            return bytes;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void writeFile(byte[] bytes, String filename) throws Exception {
        try(FileOutputStream fileOutputStream = context.openFileOutput(filename,Context.MODE_PRIVATE)){
            fileOutputStream.write(bytes);
        }
    }
}
