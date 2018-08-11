package com.dingmouren.libcamera;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.mCamera = openCamera();
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    }

    /**
     * 设置相机参数
     */
    public void setCameraParams(){

        if (mCamera == null){
            Log.e(TAG,"mCamera对象为空");
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();

        /*设置预览宽高*/
        Camera.Size previewSize = getBestSize(mSurfaceViewWidth,mSurfaceViewHeight,parameters.getSupportedPreviewSizes());
        Log.e(TAG,"设置预览宽高:"+previewSize.width+"/"+previewSize.height);
        parameters.setPreviewSize(previewSize.width,previewSize.height);

        /*设置图片宽高*/
        Camera.Size pictureSize = getBestSize(mSurfaceViewWidth,mSurfaceViewHeight,parameters.getSupportedPictureSizes());
        Log.e(TAG,"设置图片宽高:"+pictureSize.width+"/"+pictureSize.height);
        parameters.setPictureSize(pictureSize.width,pictureSize.height);

        /*设置屏幕方向*/
        if (getContext().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
        } else {
            mCamera.setDisplayOrientation(0);
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
    protected Camera.Size getBestSize(int surfaceWidth, int surfaceHeight,
                                            List<Camera.Size> sizeList) {

        int ReqTmpWidth;
        int ReqTmpHeight;
        // 当屏幕为垂直的时候需要把宽高值进行调换，保证宽大于高
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ReqTmpWidth = surfaceHeight;
            ReqTmpHeight = surfaceWidth;
        } else {
            ReqTmpWidth = surfaceWidth;
            ReqTmpHeight = surfaceHeight;
        }
        //先查找preview中是否存在与surfaceview相同宽高的尺寸
        Log.e(TAG,"支持的尺寸:");
        for(Camera.Size size : sizeList){
            Log.e(TAG,size.width+" * "+ size.height);
            if((size.width == ReqTmpWidth) && (size.height == ReqTmpHeight)){
                Log.e(TAG,"尺寸1："+size.width+"/"+size.height);
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

        Log.e(TAG,"尺寸2："+retSize.width+"/"+retSize.height);

        return retSize;
    }
}
