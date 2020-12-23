package com.android.guocheng.easypr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.activity.ReadActivity;

public class CarActivity extends AppCompatActivity {
    private TextView tvRead;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        tvRead = findViewById(R.id.tv_read);
        tvResult = findViewById(R.id.tv_result);
        tvRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadActivity.startActivity(CarActivity.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReadActivity.REQUEST_READ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String s = data.getStringExtra(ReadActivity.READ_RESULT);
                    tvResult.setText(s);
                }
            }
        }
    }
}
