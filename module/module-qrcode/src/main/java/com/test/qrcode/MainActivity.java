package com.test.qrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.fm.library.common.constants.QRCodeMsg;
import com.fm.library.common.constants.RouterPath;
import com.luo.module.qrcode.R;
import com.test.qrcode.camera.utils.FileUtil;
import com.test.qrcode.camera.utils.ZXingUtils;

@Route(path = RouterPath.QRCode.PAGE_QRCODE)
public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE_ADDRESS = 100;
    private final String[] permissions = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //创建两个Bitmap,一个放二维码，一个放logo
    private Bitmap codeBmp, logoBmp;
    private String url = "0.15747465 0.096428744 0.023635183 -0.066849805 0.07188452 -0.1644673 0.02148494 0.2378902 -0.013793017 0.009483792 -0.015340142 0.079436585 -0.02564557 -0.0013406236 0.065136604 -0.005965611 -0.1189451 0.10502972 0.051326107 4.971342E-4 0.044962786 0.033774536 -0.044822935 -0.14020278 -0.05457769 0.026327355 -0.04817941 0.09069477 -0.012272114 -0.03510314 -0.09356175 0.0063633183 -0.021869536 -0.014938065 -0.0019000361 -0.04454323 0.016319115 -0.16432744 -0.12621747 -0.03435143 0.013469607 -0.06115079 -0.10873582 -0.15985215 0.04412367 -0.11405024 -0.06660506 0.015733479 -0.07670945 -0.095449775 0.087618 -0.043843962 -0.07258379 0.1973328 0.04755007 -0.08880675 -0.11258178 0.043809 0.008260077 0.073562756 -0.014055242 4.0071204E-4 0.008915639 0.060661305 -0.020628339 0.03755057 0.023145696 -0.061290644 -0.0444733 0.14320962 0.10782678 -0.15663552 -0.027306328 0.23495328 0.026624544 -0.08985565 0.106288396 0.02564557 -0.14097197 0.11481944 0.026309874 -0.2155137 -0.04199091 0.10349133 -0.107547075 -0.04073223 -0.015689775 -0.055766445 0.1328605 -0.14062235 0.023145696 -0.109085456 -0.0011668997 -0.09356175 -0.12006393 -0.028565006 -0.08349233 0.06324859 -0.09978522 0.015663553 0.14306977 -0.16306877 0.0703811 0.09377154 0.09475051 -0.036536634 0.11370061 -0.08286299 -0.02935168 0.058983065 0.09013535 0.058004092 -0.020558413 -0.06615054 -0.08139453 0.008216373 0.12020378 -0.04807452 -0.1441886 -0.07859747 0.0027555441 0.21369562 0.011476699 -0.00978098 -0.07027621 0.07649967 0.045032714 0.07349283";
    private TextView tvSaveCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_qrcode);
        tvSaveCode = findViewById(R.id.tv_save_code);
        findViewById(R.id.tv_create_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果需要logo圆角的话可以对bitmap进行圆角处理或者图片用圆角图片
                logoBmp = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
                codeBmp = ZXingUtils.createQRImage(url, logoBmp);
                ((ImageView) findViewById(R.id.image)).setImageBitmap(codeBmp);
                tvSaveCode.setVisibility(codeBmp != null ? View.VISIBLE : View.GONE);
            }
        });
        findViewById(R.id.tv_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < permissions.length; i++) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(MainActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED
                            || ContextCompat.checkSelfPermission(MainActivity.this, permissions[2]) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE_ADDRESS);
                    } else {
                        Intent intent = new Intent(MainActivity.this, ScanningQRCodeActivity.class);
                        startActivityForResult(intent, 1000);
                        break;
                    }
                }
            }
        });
        tvSaveCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileUtil.saveImageToGallery(MainActivity.this,codeBmp);
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, ScanningQRCodeActivity.class);
                startActivityForResult(intent, 1000);
            }
        }, 600);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String resultText = data.getStringExtra("resultText");
                Toast.makeText(this, resultText, Toast.LENGTH_LONG).show();
                QRCodeMsg.INSTANCE.setName(resultText);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ADDRESS) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(this.permissions)) {
                    Intent intent = new Intent(MainActivity.this, ScanningQRCodeActivity.class);
                    startActivity(intent);
                    break;
                }
            }
        }
    }
}