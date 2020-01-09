package com.wrbug.dumpdex;

import android.util.Log;

public class Native {

    private static final String TAG = "@sun@Native";

    static {
        try {
            /* Permission deny */
            // System.load("/data/local/tmp/libdexdumper.so");

            /* Not accessible for the namespace "classloader-namespace" */
            System.load("/sdcard/armeabi-v7a/libdexdumper.so");
        } catch (Throwable t) {
            Log.d(TAG, "fail to load dexdumper: " + t);
        }
    }

    public static native void dump(String pkg);

}
