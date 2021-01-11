package com.incomingcall;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;
import android.net.Uri;
import android.os.Vibrator;
import android.content.Context;
import android.media.MediaPlayer;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.media.RingtoneManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

public class UnlockScreenActivity extends AppCompatActivity implements UnlockScreenActivityInterface {

    private static final String TAG = "MessagingService";
    private TextView tvName;
    private TextView tvInfo;
//    private ImageView ivAvatar;
    private  TextView mTextField;
    private String uuid = "";
    static boolean active = false;
    private static Vibrator v = (Vibrator) IncomingCallModule.reactContext.getSystemService(Context.VIBRATOR_SERVICE);
    private long[] pattern = {0, 1000, 800};
    private static Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    private static Activity fa;
    private static MediaPlayer player = MediaPlayer.create(IncomingCallModule.reactContext, notification);
    private static CountDownTimer countDownTimer;
    private ProgressBar mProgressBar;
    private static ReadableArray values;

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fa = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        setContentView(R.layout.activity_call_incoming);

        mTextField = findViewById(R.id.txtTimeout);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("uuid")) {
                uuid = bundle.getString("uuid");
            }
            if (bundle.containsKey("timeout")) {
                int timeout = bundle.getInt("timeout");
                startTimer(timeout);
            }
        }

        final ListView listview = (ListView) findViewById(R.id.listview);

        ArrayList<Object> data = values.toArrayList();
        ArrayList<AddressList> addressValues = new ArrayList<AddressList>();

        for (int i = 0; i < data.size(); i++) {
            ReadableMap item = values.getMap((int) i);
            addressValues.add(new AddressList(item.getString("name"), item.getString("address")));
        }

        AddressArrayAdapter adapter = new AddressArrayAdapter(this, addressValues);
        listview.setAdapter(adapter);


//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        v.vibrate(pattern, 0);

        AnimateImage acceptCallBtn = findViewById(R.id.ivAcceptCall);
        acceptCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    v.cancel();
                    if (player.isPlaying()) {
                        player.stop();
                    }
                    acceptDialing();
                } catch (Exception e) {
                    WritableMap params = Arguments.createMap();
                    params.putString("message", e.getMessage());
                    sendEvent("error", params);
                    dismissDialing();
                }
            }
        });

        AnimateImage rejectCallBtn = findViewById(R.id.ivDeclineCall);
        rejectCallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v.cancel();
                if (player.isPlaying()) {
                    player.stop();
                }
                dismissDialing();
            }
        });

    }

    public static void setAddress(ReadableArray data) {
        values = data;
    }

    private void startTimer(long totalTimeCountInMilliseconds) {
        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("sound")) {
            String sound = bundle.getString("sound", "");
            
            player = MediaPlayer.create(IncomingCallModule.reactContext, notification);

            if (sound.equalsIgnoreCase("dial_notification_1.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_1);
            } else if (sound.equalsIgnoreCase("dial_notification_2.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_2);
            } else if (sound.equalsIgnoreCase("dial_notification_3.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_3);
            } else if (sound.equalsIgnoreCase("dial_notification_4.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_4);
            } else if (sound.equalsIgnoreCase("dial_notification_5.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_5);
            } else if (sound.equalsIgnoreCase("dial_notification_6.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_6);
            } else if (sound.equalsIgnoreCase("dial_notification_7.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_7);
            } else if (sound.equalsIgnoreCase("dial_notification_8.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_8);
            } else if (sound.equalsIgnoreCase("dial_notification_9.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_9);
            } else if (sound.equalsIgnoreCase("dial_notification_10.mp3")) {
                notification = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getActContext().getPackageName() + "/" + R.raw.notification_10);
            }
        }

        try {
            if (player.isPlaying()) {
                player.stop();
            }
            player = MediaPlayer.create(IncomingCallModule.reactContext, notification);

            player.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        int maxProcessBar = (int) totalTimeCountInMilliseconds / 1000;
        mProgressBar.setMax(maxProcessBar);
        mProgressBar.setProgress(maxProcessBar);

        countDownTimer = new CountDownTimer(totalTimeCountInMilliseconds, 1000) {
            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / 1000;

                mProgressBar.setProgress((int) seconds);

                if (seconds < 10) {
                    mTextField.setText("00:0" + millisUntilFinished / 1000);
                } else {
                    mTextField.setText("00:" + millisUntilFinished / 1000);
                }

                if ((seconds % 5) == 0) {
                    try {
                        if (player.isPlaying()) {
                            player.stop();
                        }
                        player = MediaPlayer.create(IncomingCallModule.reactContext, notification);
                        player.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFinish() {
                mTextField.setText("done!");
                v.cancel();
                if (player.isPlaying()) {
                    player.stop();
                }
                dismissDialing();
            }
        }.start();
    }

    public Context getActContext() {
        return IncomingCallModule.reactContext;
    }

    @Override
    public void onBackPressed() {
        // Dont back
    }

    public static void dismissIncoming() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        v.cancel();
        player.stop();
        fa.finish();
    }

    private void acceptDialing() {
        WritableMap params = Arguments.createMap();
        params.putBoolean("accept", true);
        params.putString("uuid", uuid);
        if (!IncomingCallModule.reactContext.hasCurrentActivity()) {
            params.putBoolean("isHeadless", true);
        }

        sendEvent("answerCall", params);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        finish();
    }

    private void dismissDialing() {
        WritableMap params = Arguments.createMap();
        params.putBoolean("accept", false);
        params.putString("uuid", uuid);
        if (!IncomingCallModule.reactContext.hasCurrentActivity()) {
            params.putBoolean("isHeadless", true);
        }

        sendEvent("endCall", params);

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        finish();
    }

    @Override
    public void onConnected() {
        Log.d(TAG, "onConnected: ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onDisconnected() {
        Log.d(TAG, "onDisconnected: ");

    }

    @Override
    public void onConnectFailure() {
        Log.d(TAG, "onConnectFailure: ");

    }

    @Override
    public void onIncoming(ReadableMap params) {
        Log.d(TAG, "onIncoming: ");
    }

    private void sendEvent(String eventName, WritableMap params) {
        IncomingCallModule.reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
