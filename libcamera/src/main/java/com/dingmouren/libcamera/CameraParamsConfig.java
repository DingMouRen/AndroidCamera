package com.dingmouren.libcamera;

import android.hardware.Camera;

import static android.hardware.Camera.Parameters.FOCUS_MODE_AUTO;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 */

public class CameraParamsConfig {

    /*闪光灯相关*/
    private int mCurrentFlashModeIndex = 0;
    public final String[]  mFlashModes = new String[]{
            Camera.Parameters.FLASH_MODE_AUTO,/*系统决定是否开启闪光灯*/
            Camera.Parameters.FLASH_MODE_ON,/*拍照时打开闪光灯*/
            Camera.Parameters.FLASH_MODE_TORCH,/* 手电筒模式 一直开着闪光灯*/
            Camera.Parameters.FLASH_MODE_OFF,/*拍照时始终关闭闪光灯*/
    };

    /**
     * 获取当前的闪光灯模式
     * @return
     */
    public String getFlashMode() {
        return mFlashModes[mCurrentFlashModeIndex];
    }

    /**
     * 返回当前的闪光灯模式的索引
     * @return
     */
    public int getFlashModeIndex(){
        return mCurrentFlashModeIndex;
    }

    /**
     * 切换闪光灯模式
     */
    public void nextFlashMode(){
        this.mCurrentFlashModeIndex = (mCurrentFlashModeIndex+1) % mFlashModes.length;
    }

    /*聚焦模式相关*/
    private int mCurrentFocusModeIndex = 0;
    private String[] mFocusModes = new String[]{
            Camera.Parameters.FOCUS_MODE_AUTO,/*自动对焦模式*/
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE,/*用于拍照的连续自动对焦模式*/
            Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO,/*用于视频录制的连续自动对焦模式*/
            Camera.Parameters.FOCUS_MODE_INFINITY,/*远景模式*/
            Camera.Parameters.FOCUS_MODE_MACRO,/*微距（特写）对焦模式*/
    };

    /**
     * 获取当前的聚焦模式
     * @return
     */
    public String getFocusMode(){
        return mFocusModes[mCurrentFocusModeIndex];
    }

    /**
     * 返回当前聚焦模式的索引
     * @return
     */
    public int getFocusModeIndex(){
        return mCurrentFocusModeIndex;
    }

    /**
     * 切换聚焦模式
     */
    public void nextFocusMode(){
        this.mCurrentFocusModeIndex = (mCurrentFocusModeIndex + 1) % mFocusModes.length;
    }

}
