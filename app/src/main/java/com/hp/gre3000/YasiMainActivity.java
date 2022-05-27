package com.hp.gre3000;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hp.gre3000.utils.StatusBarUtils;

public class YasiMainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;
    private TextView tv_4;

    public static void start(Context context) {
        Intent intent = new Intent(context, YasiMainActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setDarkFontStatus(this);
        setContentView(R.layout.activity_yasi_main);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        tv_3.setOnClickListener(this);
        tv_4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                LearningActivity.start(this, "yasi0.json");
                break;
            case R.id.tv_2:
                LearningActivity.start(this, "yasi1.json");
                break;
            case R.id.tv_3:
                LearningActivity.start(this, "yasi2.json");
                break;
            case R.id.tv_4:
                LearningActivity.start(this, "yasi3.json");
                break;

        }
    }
}