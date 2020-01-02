package com.xiaomeng.workphone;

import android.util.Log;

public class Mp3Converter {

    public static native int decode(String amr, String mp3);

    static {
        try {
            System.loadLibrary("silk");
            Log.i("@sun@JNI", "load silk success!");
        } catch (Throwable e) {
            Log.e("@sun@JNI", "load silk failed!", e);
        }
    }
}
