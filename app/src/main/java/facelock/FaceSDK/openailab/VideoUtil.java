package com.openailab.sdkdemo;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import com.moonma.FaceSDK.CameraLightUtil;

/**
 * created by LiGuang
 * on 2018/11/12
 */
public class VideoUtil implements SurfaceHolder.Callback, Camera.PreviewCallback, Camera.ErrorCallback {
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private Mat mRgba;
    private boolean syncFlag = false;
    private Activity mainActivity;
    private int mWidth;
    private int mHeight;
    private int cameraId;

    public final int camWidth = 640;
    public final int camHeight = 480;

    public VideoUtil(SurfaceHolder surfaceHolder, int mWidth, int mHeight, Activity ac, int cameraId) {
        this.cameraId = cameraId;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.surfaceHolder = surfaceHolder;
        this.mainActivity = mainActivity;
        surfaceHolder.addCallback(this);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mCamera != null) {
            synchronized (this) {
                //  mCamera.addCallbackBuffer(buffers);
                mRgba.put(0, 0, data);
                syncFlag = true;

                //摄像头识别环境亮度
                CameraLightUtil.main().OnProcessLight(camera, data);
            }
            camera.addCallbackBuffer(data);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
        int test =0;
        test =0;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        int test =0;
        test =0;
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        int test =0;
        test =0;
        //@moon 切换到后台等情况之类需要销毁相机
        stopPreview();
    }


    /**
     * 开始预览
     */
    private void startPreview() {
        try {

            if (mRgba == null) {
                mRgba = new Mat(mWidth, mHeight, CvType.CV_8UC1);
            }

            //SurfaceView初始化完成，开始相机预览
            mCamera = Camera.open(cameraId);

            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setPreviewSize(camWidth, camHeight);
//            if (mainActivity.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
//                parameters.set("orientation", "portrait");
//                mCamera.setDisplayOrientation(90);
//                parameters.setRotation(90);
//            }
            mCamera.setParameters(parameters);
            mCamera.setPreviewDisplay(surfaceHolder);
//            if (cameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                SurfaceTexture mSurfaceTexture = new SurfaceTexture(9);
//                mCamera.setPreviewTexture(mSurfaceTexture);
//            }
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            mCamera.setErrorCallback(this);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mRgba.release();
            syncFlag = false;
            mCamera = null;
            mRgba = null;
        }
    }

    public Mat getmRgba() {
        return mRgba;
    }


    public boolean isSyncFlag() {
        return syncFlag;
    }

    @Override
    public void onError(int error, Camera camera) {

        Log.d("zheng", "camera error:" + error);
    }
}
