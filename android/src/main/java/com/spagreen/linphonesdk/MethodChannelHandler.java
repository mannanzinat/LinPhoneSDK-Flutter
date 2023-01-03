package com.spagreen.linphonesdk;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Map;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

public class MethodChannelHandler extends FlutterActivity implements MethodChannel.MethodCallHandler {
    private final String TAG = MethodChannelHandler.class.getSimpleName();
    private EventChannelHelper loginEventListener;
    private EventChannelHelper callEventListener;
    private LinPhoneHelper linPhoneHelper;
    private Activity activity;

    public MethodChannelHandler( Activity activity,
                                EventChannelHelper loginEventListener, EventChannelHelper callEventListener) {

        this.loginEventListener = loginEventListener;
        this.callEventListener = callEventListener;
        this.linPhoneHelper = new LinPhoneHelper(activity, loginEventListener, callEventListener);
        this.activity = activity;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        switch (call.method) {

            case "login":
                Map data = (Map) call.arguments;
                String userName = (String) data.get("userName");
                String domain = (String) data.get("domain");
                String password = (String) data.get("password");
                linPhoneHelper.login(userName, domain, password);
                result.success("Success");
                break;
            case "remove_listener":
                linPhoneHelper.removeLoginListener();
                result.success(true);
                break;
            case "remove_call_listener":
                linPhoneHelper.removeCallListener();
                result.success(true);
                break;
            case "hangUp":
                linPhoneHelper.hangUp();
                result.success(true);
            case "call":
                Map callData = (Map) call.arguments;
                String number = (String) callData.get("number");
                linPhoneHelper.call(number);
                result.success(true);
                break;
            case "request_permissions":
                try {
                    String[] permissionArrays = new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.USE_SIP,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.CHANGE_NETWORK_STATE,
                            Manifest.permission.ACCESS_WIFI_STATE,
                            Manifest.permission.CHANGE_WIFI_STATE,
                    };
                    boolean isSuccess = new Utils().checkPermissions(permissionArrays, activity);
                    if (isSuccess) {
                        result.success(true);
                    } else {
                        result.error("Permission Error", "Permission is not granted.", "Error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.error(null, e.toString(), null);
                }

                break;

            default:
                result.notImplemented();
                break;
        }
    }
}
