package com.aiqing.basiclibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.huxq17.xprefs.LogUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Utils {

    public static String sortHashMap(LinkedHashMap hashMap) {
        Object[] key = hashMap.keySet().toArray();
        Arrays.sort(key);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length; i++) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            try {
                sb.append(key[i]).append("=").append(URLDecoder.decode("" + hashMap.get(key[i]), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 把数据源HashMap转换成json
     *
     * @param map
     */
    public static String hashMapToJson(HashMap map) {
        StringBuilder sb = new StringBuilder("{");
        for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry e = (Map.Entry) it.next();
            sb.append("\"" + e.getKey() + "\":");
            Object value = e.getValue();
            LogUtils.e("value=" + value + ";type=" + value.getClass().getCanonicalName());
            if (value.getClass() == String.class) {

                if (TextUtils.isDigitsOnly((CharSequence) value)) {
                    sb.append(e.getValue() + ",");
                } else {
                    sb.append("\"" + e.getValue() + "\",");
                }
            } else {
                sb.append(e.getValue() + ",");
            }
        }
        return sb.toString().substring(0, sb.lastIndexOf(",")) + "}";
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (versionName == null || versionName.length() <= 0) {
            versionName = "";
        }

        return versionName;
    }

    /**
     * 从路径中获取文件名称
     *
     * @param path 下载路径
     * @return
     */
    public static String getFilename(String path) {
        int start = path.lastIndexOf("/");
        int end = path.indexOf("?");
        String endPath = null;
        if (start < 0) {
            return UUID.randomUUID().toString();
        }
        if (end > 0 && end > start) {
            endPath = path.substring(start + 1, end);
        } else {
            endPath = path.substring(start + 1);
        }
        return endPath;
    }

}
