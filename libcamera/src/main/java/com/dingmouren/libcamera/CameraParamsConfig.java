package com.dingmouren.libcamera;

import android.hardware.Camera;

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
        mCurrentFlashModeIndex = (mCurrentFlashModeIndex+1) % mFlashModes.length;
    }
}
