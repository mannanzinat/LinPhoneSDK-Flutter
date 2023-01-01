package com.spagreen.linphonesdk;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;

/**
 * LinphonesdkPlugin
 */
public class LinphonesdkPlugin implements FlutterPlugin, ActivityAware {
    private MethodChannel channel;
    private EventChannelHelper loginEventListener;
    private EventChannelHelper callEventListener;
    private ActivityPluginBinding activityPluginBinding;
    //event channel
    private EventChannel eventChannel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "linphonesdk");
        loginEventListener = new EventChannelHelper(flutterPluginBinding.getBinaryMessenger(), "linphonesdk/login_listener");
        callEventListener = new EventChannelHelper(flutterPluginBinding.getBinaryMessenger(), "linphonesdk/call_event_listener");
        MethodCallHandler methodCallHandler = new MethodChannelHandler(flutterPluginBinding.getApplicationContext(), activityPluginBinding, loginEventListener, callEventListener);
        channel.setMethodCallHandler(methodCallHandler);

        //event channel setup
        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "eventChannel");
        eventChannel.setStreamHandler(new MyStreamHandler(flutterPluginBinding.getApplicationContext()));
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
        this.activityPluginBinding = null;
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activityPluginBinding = binding;
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activityPluginBinding = binding;
    }

    @Override
    public void onDetachedFromActivity() {

    }
}
