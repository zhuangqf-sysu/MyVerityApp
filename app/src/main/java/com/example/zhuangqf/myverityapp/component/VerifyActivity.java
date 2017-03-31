package com.example.zhuangqf.myverityapp.component;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.zhuangqf.myverityapp.App;
import com.example.zhuangqf.myverityapp.R;
import com.example.zhuangqf.myverityapp.model.VerifyModel;
import com.example.zhuangqf.myverityapp.service.MyInternetTask;
import com.google.gson.Gson;

public class VerifyActivity extends AppCompatActivity {


    private EditText usernameText;
    private EditText passwordText;
    private Button verifyButton;
    private Button cancleButton;
    private VerifyModel verifyModel;
    private App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        usernameText = (EditText) this.findViewById(R.id.company_text);
        passwordText = (EditText) this.findViewById(R.id.password_text);
        verifyButton = (Button) this.findViewById(R.id.verify_button);
        cancleButton = (Button) this.findViewById(R.id.cancel_button);
        app = (App) this.getApplication();

        verifyModel = new VerifyModel();
        verifyModel.setMac(android.os.Build.SERIAL);

        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StartActivity.class);
                setResult(App.VERITY_RESULT_FAILURE, intent);
                finish();
            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verifyModel.setUsername(usernameText.getText().toString());
                verifyModel.setPassword(passwordText.getText().toString());

                Gson gson = new Gson();
                String message = gson.toJson(verifyModel);

                Log.e(this.getClass().getName(), message);
                new MyInternetTask(v.getContext(),app).execute(app.sslContext, "112.74.22.182:3000/verify", message);
            }
        });

        View layout = findViewById(R.id.verity_view);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(app.isDownload){
                    Intent intent = new Intent(v.getContext(),StartActivity.class);
                    setResult(App.VERITY_RESULT_SUCCESS,intent);
                    finish();
                }
            }
        });
    }
}
