package com.example.zhuangqf.myverityapp.service;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhuangqf on 3/11/17.
 */
public class CryptoService {
    private String encryption = "AES/CBC/PKCS5Padding";  // encription/cipher/padding
    private byte[] iv = new byte[]{0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8};

    public CryptoService(){}

    public CryptoService(String encryption){
        this.encryption = encryption;
    }

    public byte[] encrypt(byte[] clearText,String key) throws Exception{
        byte[] raw = MD5(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw,"AES");
        Cipher cipher = Cipher.getInstance(encryption);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);
        byte[] cipherText = cipher.doFinal(clearText);
        return Base64.encode(cipherText, Base64.DEFAULT);
//        BASE64Encoder().encode(cipherText);
    }

    public byte[] decrypt(byte[] cipherText,String key) throws Exception{
        byte[] raw = MD5(key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(raw,"AES");
        Cipher cipher = Cipher.getInstance(encryption);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);
//        byte[] tempText = new BASE64Decoder().decodeBuffer(cipherText);
        byte[] tempText = Base64.decode(cipherText,Base64.DEFAULT);
        byte[] clearText = cipher.doFinal(tempText);
        return clearText;
    }

    public byte[] MD5(String text) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        return messageDigest.digest(text.getBytes());
    }

    public String getProduceId(String username,String password,String mac,String uuid) throws Exception{
        String produceString = '#'+username+'#'+password+'#'+ uuid;
        return new String(encrypt(produceString.getBytes(),mac));
    }
}
