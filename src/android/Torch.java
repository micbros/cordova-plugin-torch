package org.apache.cordova.plugin;



import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;

// Kamera
import android.content.Context;
import android.hardware.camera2.*;

/**
 *
 */
public class Torch extends CordovaPlugin{

    boolean on = false;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("toggleTorch")) {
            String message = args.getString(0);
            try {
                this.toggleTorch(message, callbackContext);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    private void toggleTorch(String message, CallbackContext callbackContext) throws CameraAccessException{

        CameraManager manager = (CameraManager) cordova.getActivity().getSystemService(Context.CAMERA_SERVICE);

        if (on == false) {
            manager.setTorchMode("0", true);
            callbackContext.success("torch off");
            on=true;
        } else {
            manager.setTorchMode("0", false);
            callbackContext.success("torch on");
            on=false;
        }
    }
}
