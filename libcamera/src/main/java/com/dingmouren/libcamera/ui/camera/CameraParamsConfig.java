package com.dingmouren.libcamera.ui.camera;

import android.hardware.Camera;

import java.util.ArrayList;
import java.util.List;

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

    /*场景模式相关*/
    private int mCurrentSceneModeIndex = 0;
    private List<String> mSceneModes = new ArrayList<>();

    /**
     * 初始化场景模式
     */
    public void initSceneModes(List<String> sceneModes){
        mSceneModes.clear();
        mSceneModes.addAll(sceneModes);
    }

    /**
     * 获取当前的场景模式
     */
    public String getSceneMode(){
        return mSceneModes.get(mCurrentSceneModeIndex);
    }

    /**
     * 返回当前场景模式的索引
     */
    public int getSceneModeIndex(){
        return mCurrentSceneModeIndex;
    }

    /**
     * 切换场景模式
     */
    public void nextSceneMode(){
        this.mCurrentSceneModeIndex = (mCurrentSceneModeIndex + 1) % mSceneModes.size();
    }

    /*系统自带颜色效果*/
    private int mCurrentEffectInex = 0;
    private List<String> mEffects = new ArrayList<>();

    /**
     * 初始化颜色效果的集合
     */
    public void initEffects(List<String> effects){
        mEffects.clear();
        mEffects.addAll(effects);
    }

    /**
     * 获取当前的颜色效果
     */
    public String getEffect(){
        return mEffects.get(mCurrentEffectInex);
    }

    /**
     * 返回当前颜色效果的索引
     */
    public int getEffectIndex(){
        return mCurrentEffectInex;
    }

    /**
     * 切换颜色效果
     */
    public void nextEffect(){
        this.mCurrentEffectInex = (mCurrentEffectInex + 1) % mEffects.size();
    }

}
