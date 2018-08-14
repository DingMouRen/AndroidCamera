package com.dingmouren.libcamera.task;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dingmouren
 * email: naildingmouren@gmail.com
 * github: https://github.com/DingMouRen
 * 保存图片的异步任务
 */

public class SavePhotoTask  extends AsyncTask<byte[],Integer,String>{

    private  final String TAG = this.getClass().getSimpleName();

    private Context mContext;

    private String mDirPath = Environment.getExternalStorageDirectory()+"/DCIM/Camera/";

    public SavePhotoTask(Context context){
        this.mContext = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(byte[]... bytes) {
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes[0],0,bytes[0].length);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap newBitamap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);

        File dirFile = new File(mDirPath);
        if (!dirFile.exists()) dirFile.mkdirs();
        String filePath = mDirPath +"IMG_"+ System.currentTimeMillis()+".png";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            newBitamap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String filePath) {
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filePath)));
        Toast.makeText(mContext,"图片保存在:"+filePath,Toast.LENGTH_SHORT).show();
    }
}
