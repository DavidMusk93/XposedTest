package com.example.xposedtest.test;

import android.util.Log;

import com.example.xposedtest.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FetchModuleList {

    public static void fetchList() throws IOException {
        final String filePattern = "/data/data/*/conf/modules.list";
        String line;
        Process sh = Runtime.getRuntime().exec("sh");
        OutputStream os = sh.getOutputStream();
        InputStream is = sh.getInputStream();
        os.write(String.format("for i in %s; do _C=`cat $i`; echo $_C; done", filePattern).getBytes());
        os.write('\n');
        os.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
            Log.d(MainActivity.Companion.getTAG(), String.format("fetchList: ", line));
        }
    }
}
