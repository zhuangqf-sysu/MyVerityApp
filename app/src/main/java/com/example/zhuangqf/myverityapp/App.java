package com.example.zhuangqf.myverityapp;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build;

import com.example.IHelloWorld;
import com.example.zhuangqf.myverityapp.service.MyTrustManager;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

/**
 * Created by zhuangqf on 3/10/17.
 */

public class App extends Application {

    public static final int VERITY_REQUEST_CODE = 1;
    public static final int VERITY_RESULT_SUCCESS = 2;
    public static final int VERITY_RESULT_FAILURE = 3;

    public static final String preference_file_key = "com.example.zhuangqf.myverityapp.PREFERENCE_FILE_KEY";
    public static final String produce_id_key = "produceId";
    public static final String fileName = "file";
    public static final String className = "com.example.HelloAndroid";

    public SSLContext sslContext;
    public X509Certificate certificate;

    public boolean isDownload;

    private IHelloWorld helloWorld;

    @Override
    public void onCreate(){
        super.onCreate();
        initCerificate();
        initSSLContext();
        isDownload = false;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initCerificate() {
        try(InputStream in = getResources().openRawResource(R.raw.ca)){
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certificate = (X509Certificate) certificateFactory.generateCertificate(in);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initSSLContext() {
        try {
            sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new MyTrustManager(certificate);
            sslContext.init(null, new TrustManager[]{
                    tm
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IHelloWorld getHelloWorld() {
        return helloWorld;
    }

    public void setHelloWorld(IHelloWorld helloWorld) {
        this.helloWorld = helloWorld;
    }
}
