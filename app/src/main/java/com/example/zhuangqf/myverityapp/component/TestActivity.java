package com.example.zhuangqf.myverityapp.component;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.IHelloWorld;
import com.example.zhuangqf.myverityapp.App;
import com.example.zhuangqf.myverityapp.R;
import com.example.zhuangqf.myverityapp.service.ClassService;
import com.example.zhuangqf.myverityapp.service.CryptoService;
import com.example.zhuangqf.myverityapp.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class TestActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FileService fileService = new FileService(this);

        try {

//            byte[] bytes = fileService.getByteArrayFromRawId(R.raw.hello_dex);

            byte[] bytes = getBytes();


            ClassService classService = new ClassService(this);
            IHelloWorld helloWorld = (IHelloWorld) classService.loadClass(bytes,"com.example.HelloAndroid");
            Toast.makeText(this,helloWorld.sayHello(),Toast.LENGTH_LONG).show();

        }catch (Exception e){
            e.printStackTrace();
        }

//        if(copyRawFileToFileDir(R.raw.hello_dex,"test.jar")){
//
//            File file = new File(this.getFilesDir(),"test.jar");
//
//            try {
//                DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(),
//                        this.getCacheDir().getAbsolutePath(), null, this.getClassLoader());
//                Class clazz = classLoader.loadClass("com.example.HelloAndroid");
//                IHelloWorld helloWorld = (IHelloWorld) clazz.newInstance();
//                Toast.makeText(this,helloWorld.sayHello(),Toast.LENGTH_LONG).show();
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }else{
//            Toast.makeText(this,"read failure!",Toast.LENGTH_LONG).show();
//        }

    }

    public byte[] getBytes() throws Exception{
        FileService fileService = new FileService(this);
        CryptoService cryptoService = new CryptoService();
        byte[] data = fileService.getByteArrayFromRawId(R.raw.hello_dex);
        byte[] temp1 = fileService.getByteArrayFromRawId(R.raw.response);
        byte[] temp2 = cryptoService.decrypt(temp1,"zhuangqf");
        byte[] temp3 = cryptoService.decrypt(temp2,"chuyj");
        Log.e(TAG+"data:",new String(data));
        Log.e(TAG+"temp1:",new String(temp1));
        Log.e(TAG+"temp2:",new String(temp2));
        Log.e(TAG+"temp3:",new String(temp3));
        Log.e(TAG, String.valueOf(Arrays.equals(data, temp3)));
        return temp3;
    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private boolean copyRawFileToFileDir(int id,String fileName){
//        StringBuilder result = new StringBuilder();
//        try (
//                InputStream inputStream = this.getResources().openRawResource(id);
//                FileOutputStream outputStream = this.openFileOutput(fileName,MODE_PRIVATE);
//        ) {
//
//            byte[] buffer = new byte[1024];
//            int n;
//            while ((n = inputStream.read(buffer,0,buffer.length))>0){
//                outputStream.write(buffer,0,n);
//                result.append(new String(buffer, 0, n));
//                Log.e(this.getClass().getName(), "result`s length:" + result.length());
//            }
//            Log.e(this.getClass().getName(),result.toString());
//            Log.e(this.getClass().getName(),"result`s all length:" + result.length());
//            return true;
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }

//    public byte[] getBytes() throws Exception{
//
//        FileService fileService = new FileService(this);
//
//        byte[] bytes = fileService.getByteArrayFromRawId(R.raw.response);
//        bytes = CryptoService.decrypt(bytes,"mac");
//        bytes = CryptoService.decrypt(bytes,"194209107ae68c5feb0d344c6166142bc55d0260da23d50108ba1e9391e11b6f363feda6834f2b5301ac8caaf4995c7a2a7cb738b00904146e438bac926d15d1");
//
//        byte[] pre = fileService.getByteArrayFromRawId(R.raw.hello_dex);
//        if(pre.equals(bytes)){
//            Log.e(TAG,"equal");
//        }else {
//            Log.e(TAG,"not equal");
//            Log.e(TAG," pre`s Length:"+pre.length);
//            Log.e(TAG,"byte`s Length:"+bytes.length);
//            for(int i=0;i<pre.length&&i<bytes.length;i++){
//                if(pre[i]!=bytes[i]){
//                    Log.e(TAG,"not equal in "+i+" that pre = "+pre[i]+" and byte = "+bytes[i]);
//                }
//            }
//        }
//
//        return bytes;
//    }
}
