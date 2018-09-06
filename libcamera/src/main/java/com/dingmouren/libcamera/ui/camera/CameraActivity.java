package com.dingmouren.libcamera.ui.camera;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dingmouren.libcamera.R;

import java.util.Arrays;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class CameraActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    private String[] mRequiredPermissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        ActivityCompat.requestPermissions(this, mRequiredPermissions, 1);





    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG,requestCode+" "+ Arrays.toString(permissions)+" "+Arrays.toString(grantResults));
        switch (requestCode){
            case 1:
                if (grantResults.length > 0){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_layout,new CameraFragment())
                            .commit();
                }
                break;
        }
    }
}
