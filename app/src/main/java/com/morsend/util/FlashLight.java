package com.morsend.util;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;

public class FlashLight {

    private FlashLight() {}

    private static CameraManager cameraManager = null;
    private static String cameraId = null;

    public static boolean openDevice(Activity activity) {
        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraId = cameraManager.getCameraIdList()[0];
            setLight(false);
            return true;
        } catch (CameraAccessException e) {
            // TODO exceptionManager.log
            e.printStackTrace();
            return false;
        }
    }

    public static void closeDevice() {
        setLight(false);
    }

    public static void setLight(boolean enabled) {
        if (cameraManager != null) {
            try {
                cameraManager.setTorchMode(cameraId, enabled);
            } catch (CameraAccessException e) {
                // TODO exceptionManager.log
                e.printStackTrace();
            }
        }
    }

}
