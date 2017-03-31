package com.example.zhuangqf.myverityapp.service;

import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by zhuangqf on 2/8/17.
 */
public class MyTrustManager implements X509TrustManager {

    private X509Certificate certificate;

    public MyTrustManager(X509Certificate certificate){
        this.certificate = certificate;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        for(X509Certificate cert:chain){
            if(cert.equals(certificate)){
                Log.i("debug","checkServerTrusted Certificate from server is valid!");
                return;
            }
        }
        throw new CertificateException("checkServerTrusted No trusted server cert found!");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
