package com.dingmouren.libcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.dingmouren.libcamera.listener.OnCameraParamsConfigInitedListener;
import com.dingmouren.libcamera.listener.OnEffectChangedListener;
import com.dingmouren.libcamera.listener.OnFlashModeChangedListener;
import com.dingmouren.libcamera.listener.OnFocusModeChangedListener;
import com.dingmouren.libcamera.listener.OnSceneModeChengedListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

    private final String TAG = this.getClass().getName();

    private SurfaceHolder mSurfaceHolder;

    private int mSurfaceViewWidth,mSurfaceViewHeight;

    private Camera mCamera;

    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;/*默认是后置摄像头*/

    private CameraParamsConfig mCameraParamsConfig;/*相机部分参数的持有者*/

    private OnCameraParamsConfigInitedListener mCameraConfigListener;/*CameraParamsConfig数据初始化完成的监听*/

    private OnFlashModeChangedListener mFlashModeChangedListener;/*闪光灯模式的监听*/

    private OnFocusModeChangedListener mFocusModeChangedListener;/*聚焦模式的监听*/

    private OnSceneModeChengedListener mSceneModeChengedListener;/*场景模式的监听*/

    private OnEffectChangedListener mEffectChangedListener;/*颜色效果变化的监听*/


    public CameraSurfaceView(Context context) {
        super(context);
        initView(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        this.mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mCameraParamsConfig = new CameraParamsConfig();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mCamera = openCamera();
        initCameraParamsConfig();/*初始化相机部分参数的持有者的数据*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        this.mSurfaceViewWidth = width;
        this.mSurfaceViewHeight = height;
        setCameraParams();
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开启相机
     */
    private Camera  openCamera(){
        Camera camera = null;
        try {
            camera = Camera.open();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
        return camera;
    }

    /**
     * 切换摄像头方向
     */
    public void switchCamera(){
        int camerId = 0;
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            camerId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else {
            camerId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        this.mCameraId = camerId;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = Camera.open(mCameraId);
        setCameraParams();
        mCamera.startPreview();
    }

    /**
     * 切换相机闪光灯模式
     */
    public void nextFlashMode(){
        mCameraParamsConfig.nextFlashMode();
        setCameraParams();
        mCamera.startPreview();
        if (mFlashModeChangedListener != null){
            mFlashModeChangedListener.onFlashModeChangedListener(mCameraParamsConfig.getFlashModeIndex(),mCameraParamsConfig.getFlashMode());
        }
    }

    /**
     * 切换相机的聚焦模式
     */
    public void nextFocusMode(){
        mCameraParamsConfig.nextFocusMode();
        setCameraParams();
        mCamera.startPreview();
        if (mFocusModeChangedListener != null){
            mFocusModeChangedListener.onFocusModeChangedListener(mCameraParamsConfig.getFocusModeIndex(),mCameraParamsConfig.getFocusMode());
        }
    }

    /**
     * 切换相机的场景模式
     */
    public void nextSceneMode(){
        mCameraParamsConfig.nextSceneMode();
        setCameraParams();
        mCamera.startPreview();
        if (mSceneModeChengedListener != null){
            mSceneModeChengedListener.onSceneModeChangedListener(mCameraParamsConfig.getSceneModeIndex(),mCameraParamsConfig.getSceneMode());
        }
    }

    /**
     * 切换相机的颜色效果
     */
    public void nextEffect(){
        mCameraParamsConfig.nextEffect();
        setCameraParams();
        mCamera.startPreview();
        if (mEffectChangedListener != null){
            mEffectChangedListener.onEffectChangedListener(mCameraParamsConfig.getEffectIndex(),mCameraParamsConfig.getEffect());
        }
    }

    /**
     * 设置相机参数
     */
    private void setCameraParams(){

        if (mCamera == null){
            Log.e(TAG,"mCamera对象为空");
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();

        /*设置预览宽高*/
        Camera.Size previewSize = getBestSize(mSurfaceViewWidth,mSurfaceViewHeight,parameters.getSupportedPreviewSizes());
        Log.i(TAG,"设置预览宽高:"+previewSize.width+"/"+previewSize.height);
        parameters.setPreviewSize(previewSize.width,previewSize.height);

        /*设置图片宽高*/
        Camera.Size pictureSize = getBestSize(mSurfaceViewWidth,mSurfaceViewHeight,parameters.getSupportedPictureSizes());
        Log.i(TAG,"设置图片宽高:"+pictureSize.width+"/"+pictureSize.height);
        parameters.setPictureSize(pictureSize.width,pictureSize.height);

        /*设置屏幕方向*/
        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
        } else {
            mCamera.setDisplayOrientation(0);
        }

        /*设置闪关灯模式*/
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
        parameters.setFlashMode(mCameraParamsConfig.getFlashMode());

        /*设置聚焦模式*/
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
        parameters.setFocusMode(mCameraParamsConfig.getFocusMode());

        /*设置场景模式,根据相机的支持进行设置*/
        parameters.setSceneMode(mCameraParamsConfig.getSceneMode());

        /*设置颜色效果*/
        parameters.setColorEffect(mCameraParamsConfig.getEffect());

        /*设置surfaceholder*/
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mCamera.setParameters(parameters);

    }

    /**
     * 获取合适的尺寸
     * @param surfaceWidth
     * @param surfaceHeight
     * @param sizeList
     * @return
     */
    private Camera.Size getBestSize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> sizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        for(Camera.Size size : sizeList){
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                return size;
            }
        }

        // 得到与传入的宽高比最接近的size
        float reqRatio = ((float) ReqTmpWidth) / ReqTmpHeight;
        float curRatio, deltaRatio;
        float deltaRatioMin = Float.MAX_VALUE;
        Camera.Size retSize = null;
        for (Camera.Size size : sizeList) {
            curRatio = ((float) size.width) / size.height;
            deltaRatio = Math.abs(reqRatio - curRatio);
            if (deltaRatio < deltaRatioMin) {
                deltaRatioMin = deltaRatio;
                retSize = size;
            }
        }
        return retSize;
    }

    /**
     * 初始化相机部分参数的持有者的数据
     */
    private void initCameraParamsConfig() {
        if (mCamera == null){
            Log.e(TAG,"mCamera为空");
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();

        /*初始化场景模式数据*/
        mCameraParamsConfig.initSceneModes(parameters.getSupportedSceneModes());

        /*初始化颜色效果的集合数据*/
        mCameraParamsConfig.initEffects(parameters.getSupportedColorEffects());

        if (mCameraConfigListener != null) mCameraConfigListener.onComplete();
    }

    /**
     * 设置CameraParamsConfig数据初始化完成的监听
     */
    public void setOnCameraParamsConfigInitedListener(OnCameraParamsConfigInitedListener listener){
        this.mCameraConfigListener = listener;
    }

    /**
     * 设置闪光灯模式变化的监听
     * @param listener
     */
    public void setOnFlashModeChangedListener(OnFlashModeChangedListener listener){
        this.mFlashModeChangedListener = listener;
    }

    /**
     * 设置聚焦模式的监听
     * @param listener
     */
    public void setOnFocusModeChangedListener(OnFocusModeChangedListener listener){
        this.mFocusModeChangedListener = listener;
    }

    /**
     * 设置场景模式的监听
     * @param listener
     */
    public void setOnSceneModeChengedListener(OnSceneModeChengedListener listener){
        this.mSceneModeChengedListener = listener;
    }

    /**
     * 设置颜色效果的监听
     * @param listener
     */
    public void setOnEffectChangedListener(OnEffectChangedListener listener){
        this.mEffectChangedListener = listener;
    }

    public CameraParamsConfig getCamearaParamsConfig(){
        return mCameraParamsConfig;
    }
}
