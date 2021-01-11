package com.incomingcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.view.WindowManager;
import android.content.Context;
import android.util.Log;
import java.util.Timer;
import java.util.TimerTask;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;

public class IncomingCallModule extends ReactContextBaseJavaModule {

    public static ReactApplicationContext reactContext;
    public static Activity mainActivity;

    private static final String TAG = "RNIC:IncomingCallModule";
    private WritableMap headlessExtras;
    private ReadableMap _settings;
    private String sound;

    public IncomingCallModule(ReactApplicationContext context) {
        super(context);
        reactContext = context;
        mainActivity = getCurrentActivity();
    }

    @Override
    public String getName() {
        return "IncomingCall";
    }

    @ReactMethod
    public void configs(ReadableMap data) {
//        if (mainActivity != null) {
//            mainActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mainActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//                }
//            });
//        }

        _settings = data;
        if (_settings.hasKey("sound")) {
            this.sound = _settings.getString("sound");
        }
    }

    @ReactMethod
    public void setSound(String sound) {
        this.sound = sound;
    }

    @ReactMethod
    public void playSound(String sound) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        MediaPlayer player = MediaPlayer.create(IncomingCallModule.reactContext, notification);

        if (sound.equalsIgnoreCase("dial_notification_1.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_1);
        } else if (sound.equalsIgnoreCase("dial_notification_2.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_2);
        } else if (sound.equalsIgnoreCase("dial_notification_3.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_3);
        } else if (sound.equalsIgnoreCase("dial_notification_4.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_4);
        } else if (sound.equalsIgnoreCase("dial_notification_5.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_5);
        } else if (sound.equalsIgnoreCase("dial_notification_6.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_6);
        } else if (sound.equalsIgnoreCase("dial_notification_7.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_7);
        } else if (sound.equalsIgnoreCase("dial_notification_8.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_8);
        } else if (sound.equalsIgnoreCase("dial_notification_9.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_9);
        } else if (sound.equalsIgnoreCase("dial_notification_10.mp3")) {
            notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + IncomingCallModule.reactContext.getPackageName() + "/" + R.raw.notification_10);
        }

        player = MediaPlayer.create(IncomingCallModule.reactContext, notification);

        player.start();
    }

    @ReactMethod
    public void display(String uuid, ReadableArray address, int timeout) {
        if (UnlockScreenActivity.active) {
            return;
        }
        if (reactContext != null) {
            Intent i = new Intent(reactContext, UnlockScreenActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("uuid", uuid);
//            bundle.put("address", address.toHashMap());
//            bundle.("name", address);
//            bundle.putString("avatar", avatar);
//            bundle.putString("info", info);
            UnlockScreenActivity.setAddress(address);
            bundle.putInt("timeout", timeout);
            if (this.sound != null) {
                bundle.putString("sound", this.sound);
            }

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED +
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD +
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

            i.putExtras(bundle);
            reactContext.startActivity(i);

//            if (timeout > 0) {
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        // this code will be executed after timeout seconds
//                        UnlockScreenActivity.dismissIncoming();
//                    }
//                }, timeout);
//            }
        }
    }

    @ReactMethod
    public void dismiss() {
        // final Activity activity = reactContext.getCurrentActivity();

        // assert activity != null;

        UnlockScreenActivity.dismissIncoming();

        return;
    }

    private Context getAppContext() {
        return this.reactContext.getApplicationContext();
    }

    @ReactMethod
    public void backToForeground() {
        Context context = getAppContext();
        String packageName = context.getApplicationContext().getPackageName();
        Intent focusIntent = context.getPackageManager().getLaunchIntentForPackage(packageName).cloneFilter();
        Activity activity = getCurrentActivity();
        boolean isOpened = activity != null;
        Log.d(TAG, "backToForeground, app isOpened ?" + (isOpened ? "true" : "false"));

        if (isOpened) {
            focusIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            activity.startActivity(focusIntent);
        }
    }

    @ReactMethod
    public void openAppFromHeadlessMode(String uuid) {
        Context context = getAppContext();
        String packageName = context.getApplicationContext().getPackageName();
        Intent focusIntent = context.getPackageManager().getLaunchIntentForPackage(packageName).cloneFilter();
        Activity activity = getCurrentActivity();
        boolean isOpened = activity != null;

        if (!isOpened) {
            focusIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

            final WritableMap response = new WritableNativeMap();
            response.putBoolean("isHeadless", true);
            response.putString("uuid", uuid);

            this.headlessExtras = response;

            getReactApplicationContext().startActivity(focusIntent);
        }
    }

    @ReactMethod
    public void getExtrasFromHeadlessMode(Promise promise) {
        if (this.headlessExtras != null) {
            promise.resolve(this.headlessExtras);

            this.headlessExtras = null;

            return;
        }

        promise.resolve(null);
    }
}
