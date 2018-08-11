package com.dingmouren.libcamera;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.dingmouren.libcamera.listener.OnFlashModeChangedListener;
import com.dingmouren.libcamera.listener.OnFocusModeChangedListener;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class CameraFragment extends Fragment  {

    private CameraSurfaceView mCameraView;
    private ScrollView mLayoutTop;

    /*闪光灯有关*/
    private Button mBtnFlash;
    private String[] mFlashModeStr = new String[]{"自动","拍照模式","手电筒模式","关闭"};

    /*对焦模式相关*/
    private Button mBtnFocus;
    private String[] mFocusModeStr = new String[]{"自动对焦","拍照模式","录像模式","远景模式","微距模式"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera,container,false);

        initView(rootView);

        initListener();

        return rootView;
    }

    private void initView(View rootView) {

        mBtnFlash = rootView.findViewById(R.id.btn_flash);
        mBtnFocus = rootView.findViewById(R.id.btn_focus);
        mCameraView = rootView.findViewById(R.id.camera_view);
        mLayoutTop = rootView.findViewById(R.id.layout_top);

        /*头部工具栏的margintop的值*/
        int statusBarHeight = getStatusBarHeight(getContext());
        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,0,getContext().getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) mLayoutTop.getLayoutParams();
        params1.topMargin = statusBarHeight + marginTop;
        mLayoutTop.setLayoutParams(params1);
    }

    private void initListener(){
        /*切换闪光等模式*/
        mBtnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.nextFlashMode();
            }
        });

        /*闪光灯模式切换的监听*/
        mCameraView.setOnFlashModeChangedListener(new OnFlashModeChangedListener() {
            @Override
            public void onFlashModeChangedListener(int index, String flashMode) {
                mBtnFlash.setText(mFlashModeStr[index]);
            }
        });

        /*切换聚焦模式*/
        mBtnFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.nextFocusMode();
            }
        });

        /*聚焦模式的监听*/
        mCameraView.setOnFocusModeChangedListener(new OnFocusModeChangedListener() {
            @Override
            public void onFocusModeChangedListener(int index, String focusMode) {
                mBtnFocus.setText(mFocusModeStr[index]);
            }
        });
    }


    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    }
