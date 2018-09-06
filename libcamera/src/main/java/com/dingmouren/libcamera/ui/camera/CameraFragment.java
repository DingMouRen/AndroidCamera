package com.dingmouren.libcamera.ui.camera;

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
import android.widget.HorizontalScrollView;

import com.dingmouren.libcamera.R;
import com.dingmouren.libcamera.listener.OnCameraParamsConfigInitedListener;
import com.dingmouren.libcamera.listener.OnEffectChangedListener;
import com.dingmouren.libcamera.listener.OnFlashModeChangedListener;
import com.dingmouren.libcamera.listener.OnFocusModeChangedListener;
import com.dingmouren.libcamera.listener.OnSceneModeChengedListener;
import com.dingmouren.libcamera.view.TouchControlView;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class CameraFragment extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private CameraSurfaceView mCameraView;
    private HorizontalScrollView mLayoutTop;
    private TouchControlView mTouchControlView;


    /*闪光灯有关*/
    private Button mBtnFlash;
    private String[] mFlashModeStr = new String[]{"自动", "拍照模式", "手电筒模式", "关闭"};

    /*对焦模式相关*/
    private Button mBtnFocus;
    private String[] mFocusModeStr = new String[]{"自动对焦", "拍照模式", "录像模式", "远景模式", "微距模式"};

    /*场景模式相关*/
    private Button mBtnScene;

    /*颜色效果有关*/
    private Button mBtnEffect;

    /*切换摄像头*/
    private Button mBtnSwitchCamera;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);

        initView(rootView);

        initListener();

        return rootView;
    }

    private void initView(View rootView) {



        mBtnFlash = rootView.findViewById(R.id.btn_flash);
        mBtnFocus = rootView.findViewById(R.id.btn_focus);
        mBtnScene = rootView.findViewById(R.id.btn_scene);
        mBtnSwitchCamera = rootView.findViewById(R.id.btn_switch_camera);
        mBtnEffect = rootView.findViewById(R.id.btn_effect);
        mCameraView = rootView.findViewById(R.id.camera_view);
        mLayoutTop = rootView.findViewById(R.id.layout_top);
        mTouchControlView = rootView.findViewById(R.id.touch_control_view);

        /*头部工具栏的margintop的值*/
        int statusBarHeight = getStatusBarHeight(getContext());
        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getContext().getResources().getDisplayMetrics());
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) mLayoutTop.getLayoutParams();
        params1.topMargin = statusBarHeight + marginTop;
        mLayoutTop.setLayoutParams(params1);

    }

    private void initListener() {
        /*CameraParamsConfig数据初始化完成的监听*/
        mCameraView.setOnCameraParamsConfigInitedListener(new OnCameraParamsConfigInitedListener() {
            @Override
            public void onComplete() {
                mBtnScene.setText("场景: " + mCameraView.getCamearaParamsConfig().getSceneMode());
                mBtnEffect.setText("滤镜: " + mCameraView.getCamearaParamsConfig().getEffect());
            }
        });

        /*切换摄像头*/
        mBtnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.switchCamera();
            }
        });

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
        /*聚焦模式切换的监听*/
        mCameraView.setOnFocusModeChangedListener(new OnFocusModeChangedListener() {
            @Override
            public void onFocusModeChangedListener(int index, String focusMode) {
                mBtnFocus.setText(mFocusModeStr[index]);
            }
        });

        /*切换场景模式*/
        mBtnScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.nextSceneMode();
            }
        });
        /*场景模式切换的监听*/
        mCameraView.setOnSceneModeChengedListener(new OnSceneModeChengedListener() {
            @Override
            public void onSceneModeChangedListener(int index, String sceneMode) {
                mBtnScene.setText("场景: " + sceneMode);
            }
        });

        /*切换颜色效果*/
        mBtnEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraView.nextEffect();
            }
        });
        /*颜色效果切换的监听*/
        mCameraView.setOnEffectChangedListener(new OnEffectChangedListener() {
            @Override
            public void onEffectChangedListener(int index, String effect) {
                mBtnEffect.setText("滤镜：" + effect);
            }
        });

        /*拍照+录像*/
        mTouchControlView.setOnRecordListener(new TouchControlView.OnRecordListener() {

            /*拍照*/
            @Override
            public void onShortClick() {
                mCameraView.takePhoto();
            }

            /*录制开始*/
            @Override
            public void OnRecordStartClick() {
                mCameraView.startRecordVideo();
            }

            /*录制结束*/
            @Override
            public void OnFinish(int resultCode) {
                mCameraView.stopRecordVideo();
            }
        });
    }


    /**
     * 获取状态栏高度
     *
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
