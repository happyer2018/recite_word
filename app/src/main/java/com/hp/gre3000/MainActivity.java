package com.hp.gre3000;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hp.gre3000.utils.StatusBarUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView zx;
    private TextView lx;
    private TextView tv_ys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.setTransparentStatusImmerse(this);
        StatusBarUtils.setDarkFontStatusImmerse(this);
        setContentView(R.layout.activity_main);
        zx = findViewById(R.id.tv_zx);
        lx = findViewById(R.id.tv_lx);
        tv_ys = findViewById(R.id.tv_ys);
        zx.setOnClickListener(this);
        lx.setOnClickListener(this);
        tv_ys.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_lx:
                LearningActivity.start(this, "outputRandom.json");
//                LearningActivity.start(this, "test.json");
                break;
            case R.id.tv_zx:
                LearningActivity.start(this, "output.json");
//                LearningActivity.start(this, "test.json");
                break;
            case R.id.tv_ys:
                YasiMainActivity.start(this);
//                LearningActivity.start(this, "test.json");
                break;
        }
    }
}