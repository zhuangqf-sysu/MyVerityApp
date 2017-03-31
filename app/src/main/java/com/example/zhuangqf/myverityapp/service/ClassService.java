package com.example.zhuangqf.myverityapp.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.IHelloWorld;
import com.example.zhuangqf.myverityapp.R;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by zhuangqf on 3/5/17.
 */
public class ClassService {

    private Context context;
    private final String TEMP_NAME = "test.jar";
    private final String TAG = this.getClass().getName();

    public ClassService(Context context){
        this.context = context;
    }

    public Object loadClass(byte[] dexBytes,String className) throws Exception{

        FileService fileService = new FileService(context);

        fileService.writeFile(dexBytes,TEMP_NAME);
        File file = new File(context.getFilesDir(),TEMP_NAME);
        DexClassLoader classLoader = new DexClassLoader(file.getAbsolutePath(),
                context.getCacheDir().getAbsolutePath(), null, context.getClassLoader());
        Class clazz = classLoader.loadClass(className);
        context.deleteFile(TEMP_NAME);
        return clazz.newInstance();
    }

}
