package com.example.hbsgz.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(Context context){

        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null) {
            networkInfo = connManager.getActiveNetworkInfo();
        }
//        判断是否无网络
        if (networkInfo == null){
            return NETWORN_NONE;
        }
        int nType = networkInfo.getType();
//        判断是否有手机移动网络
        if (nType == ConnectivityManager.TYPE_MOBILE){
            return NETWORN_MOBILE;
//            判断是否有WiFi网络
        }else if (nType == ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }
}
