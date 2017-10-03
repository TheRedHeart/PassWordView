package com.lxj.passView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lxj.passView.view.PassWordLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final PassWordLayout pass = (PassWordLayout) findViewById(R.id.pa3);

        pass.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {
                Log.e("密码改变",pwd);
            }

            @Override
            public void onNull() {
                Log.e("密码改变","null");
            }

            @Override
            public void onFinished(String pwd) {
                Log.e("密码改变","结束"+pwd);
            }
        });

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass.removeAllPwd();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, pass.getPassString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
