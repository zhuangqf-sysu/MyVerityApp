package com.example.zhuangqf.myverityapp.component;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zhuangqf.myverityapp.App;
import com.example.zhuangqf.myverityapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App app = (App) this.getApplication();


        if (app.getHelloWorld() != null)
            Toast.makeText(this, app.getHelloWorld().sayHello(), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Class Loader Failure!", Toast.LENGTH_LONG).show();
    }
}
