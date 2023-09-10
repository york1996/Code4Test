package com.york1996.samplecode4varlens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.york1996.samplecode4varlens.widget.CustomSettingWindowButton;

public class MainActivity extends AppCompatActivity {

    private CustomSettingWindowButton mCustomSettingWindowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 功能选择弹窗按钮
        mCustomSettingWindowButton = findViewById(R.id.btn_pop_setting);
        mCustomSettingWindowButton.setFunctionCheck(CustomSettingWindowButton.FUNCTION_TYPE_1, true);
        mCustomSettingWindowButton.setOnClickListener(v -> {
            mCustomSettingWindowButton.showSettingWindow();
        });
        mCustomSettingWindowButton.setSettingWindowListener(new CustomSettingWindowButton.SettingWindowListener() {
            @Override
            public void onWindowShowingStateChange(boolean showing) {
                Toast.makeText(getApplicationContext(), "弹窗显示：" + showing, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFunctionCheckChange(int functionType, boolean check) {
                Toast.makeText(getApplicationContext(), "功能勾选：" + functionType + " check: " + check,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 半圆调节视图
    }
}