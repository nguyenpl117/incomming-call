package com.incomingcall;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.facebook.react.bridge.ReadableMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SocketService {
    public static Socket mSocket;

    public static void socket(String uri, ReadableMap opt) {
        try {
            mSocket = null;

            IO.Options options = new IO.Options();
            if (opt.hasKey("query")) {
                options.query = opt.getString("query");
            }
            Log.d(SocketService.class.getSimpleName(), "connect token: " + options.query);
            options.forceNew = true;
            options.secure = true;
            options.reconnection = true;
            options.transports = new String[]{"websocket"};

            mSocket = IO.socket(uri, options);
            mSocket.connect();

        } catch (URISyntaxException e) {}
    }

    public static void emit(final String event, ReadableMap opt) {
        try {
            mSocket.emit(event, ReactNativeJson.convertMapToJson(opt));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        mSocket.disconnect();
    }

}
