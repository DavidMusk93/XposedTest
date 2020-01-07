package com.izuiyou.network;

import android.util.Log;


public class NetCrypto {

    static {
        try {
            System.loadLibrary("net_crypto");
            native_init();
        } catch (Exception e) {
            Log.e("@sun@JNI", "load net_crypto failed!", e);
        }
    }

    public static String a(String str, byte[] bArr) {
        String str2;
        String sign = sign(str, bArr);
        if (str.contains("?")) {
            str2 = "&sign=" + sign;
        } else {
            str2 = "?sign=" + sign;
        }
        return str + str2;
    }

    public static String b(String str, byte[] bArr) {
        String str2;
        String generateSign = generateSign(bArr);
        if (str.contains("?")) {
            str2 = "&sign=" + generateSign;
        } else {
            str2 = "?sign=" + generateSign;
        }
        return str + str2;
    }

    public static native byte[] decodeAES(byte[] bArr, boolean z);

    public static native byte[] encodeAES(byte[] bArr);

    public static native String generateSign(byte[] bArr);

    public static native String getProtocolKey();

    public static native void setProtocolKey(String str);

    public static native void native_init();

    public static native boolean registerDID(byte[] bArr);

    public static native String sign(String str, byte[] bArr);
}
