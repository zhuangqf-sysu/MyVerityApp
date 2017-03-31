package com.example.zhuangqf.myverityapp.component;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.IHelloWorld;
import com.example.zhuangqf.myverityapp.App;
import com.example.zhuangqf.myverityapp.R;
import com.example.zhuangqf.myverityapp.service.ClassService;
import com.example.zhuangqf.myverityapp.service.CryptoService;
import com.example.zhuangqf.myverityapp.service.FileService;

import java.io.File;
import java.util.Arrays;

import dalvik.system.DexClassLoader;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        app = (App) this.getApplication();

        sharedPreferences = this.getSharedPreferences(App.preference_file_key,MODE_PRIVATE);

        View layout = findViewById(R.id.start_view);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOtherActivity(isVerified());
            }
        });
    }

    private void startOtherActivity(boolean verified) {
        if(verified){
            try {
                CryptoService cryptoService = new CryptoService();
                FileService fileService = new FileService(this);
                String key = sharedPreferences.getString(App.produce_id_key, "");
                byte[] temp1 = fileService.readFile(App.fileName);
                byte[] temp2 = cryptoService.decrypt(temp1, Build.SERIAL);
                byte[] temp3 = cryptoService.decrypt(temp2,key);
                ClassService classService = new ClassService(this);
                IHelloWorld helloWorld = (IHelloWorld) classService.loadClass(temp3, App.className);
                app.setHelloWorld(helloWorld);
            }catch (Exception e){
                e.printStackTrace();
                verifyFailure();
            }

            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(this, VerifyActivity.class);
            startActivityForResult(intent, App.VERITY_REQUEST_CODE);
        }
    }

    private void verifyFailure() {
        deleteFile(App.fileName);
        sharedPreferences.edit().remove(App.produce_id_key).apply();
        Toast.makeText(this, "激活出错，请重新激活！", Toast.LENGTH_LONG).show();
        finish();
    }

    private boolean isVerified() {
        if(!sharedPreferences.contains(App.produce_id_key)) return false;
        FileService fileService = new FileService(this);
        return fileService.isFile(App.fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==App.VERITY_REQUEST_CODE){
            switch (resultCode){
                case App.VERITY_RESULT_SUCCESS:
                    startOtherActivity(true);
                    break;
                case App.VERITY_RESULT_FAILURE:
                    verifyFailure();
                    break;
                default: super.onActivityResult(requestCode, resultCode, data);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
