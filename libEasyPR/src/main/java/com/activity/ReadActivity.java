package com.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fosung.libeasypr.R;
import com.fosung.libeasypr.view.EasyPRPreSurfaceView;
import com.fosung.libeasypr.view.EasyPRPreView;
import com.gyf.immersionbar.ImmersionBar;
import com.mj.permission.DynamicPermissionEmitter;
import com.mj.permission.DynamicPermissionEntity;

import java.util.Map;

public class ReadActivity extends AppCompatActivity {
    private EasyPRPreView easyPRPreView;
    private Button btnShutter;
    private Button btnCancel;
    private String permissionName = Manifest.permission.CAMERA;
    public static final int REQUEST_READ_CODE = 10001;
    public static final String READ_RESULT = "read_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        requestPermission();
        ImmersionBar.with(this)
                .navigationBarColor(R.color.read)
                .statusBarColor(R.color.black)
                .init();
    }

    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, ReadActivity.class);
        activity.startActivityForResult(intent, REQUEST_READ_CODE);
    }

    private void init() {
        easyPRPreView = findViewById(R.id.preSurfaceView);
        btnShutter = findViewById(R.id.btnShutter);
        btnCancel = findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initListener();
    }

    private void requestPermission() {
        DynamicPermissionEmitter permissionEmitter = new DynamicPermissionEmitter(this);
        permissionEmitter.emitterPermission(new DynamicPermissionEmitter.ApplyPermissionsCallback() {

            @Override
            public void applyPermissionResult(Map<String, DynamicPermissionEntity> permissionEntityMap) {
                DynamicPermissionEntity permissionEntity = permissionEntityMap.get(permissionName);
                if (permissionEntity != null) {
                    if (permissionEntity.isGranted()) {
                        //权限允许，可以搞事情了
                        init();
                    } else if (permissionEntity.shouldShowRequestPermissionRationable()) {
                        //勾选不在提示，且点击了拒绝，在这里给用户提示权限的重要性，给一个友好的提示
                        Toast.makeText(ReadActivity.this, "你拒绝了权限,将无法正常使用,如需使用,请到设置里面开启相机权限。", Toast.LENGTH_SHORT).show();
                    } else {
                        //拒绝了权限，不能乱搞
                        Toast.makeText(ReadActivity.this, "你拒绝了权限,将无法正常使用,如需使用,请到设置里面开启相机权限。", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, permissionName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (easyPRPreView != null) {
            easyPRPreView.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (easyPRPreView != null) {
            easyPRPreView.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (easyPRPreView != null) {
            easyPRPreView.onDestroy();
        }
    }

    private void initListener() {
        easyPRPreView.setRecognizedListener(new EasyPRPreSurfaceView.OnRecognizedListener() {
            @Override
            public void onRecognized(String result) {
                if (result == null || result.equals("0")) {
                    Toast.makeText(ReadActivity.this, "请重新试试!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReadActivity.this, "识别成功", Toast.LENGTH_SHORT).show();
                    backData(result);
                    finish();
                }
            }
        });
        btnShutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyPRPreView.recognize();//开始识别
            }
        });
    }

    private void backData(String result) {
        if (!TextUtils.isEmpty(result)) {
            Intent data = new Intent();
            data.putExtra(READ_RESULT, result);
            setResult(Activity.RESULT_OK, data);
        }
    }
}
