package com.dingmouren.androidcamera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dingmouren.libcamera.ui.camera.CameraActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    /*拍照和录像*/
    public void takePhotoAndVideo(View view){
        startActivity(new Intent(this, CameraActivity.class));
    }

    /*硬编硬解*/
    public void videoEncode(View view){

    }
}
