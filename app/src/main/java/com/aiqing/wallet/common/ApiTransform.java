package com.aiqing.wallet.common;

import com.aiqing.basiclibrary.utils.Device;
import com.aiqing.wallet.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ApiTransform {
    public static RequestBody transform(Map<String, Object> params) {
        params.put("udid", Device.getDeviceId(App.getContext()));
        JSONObject result = new JSONObject();
        for (Iterator it = params.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<String, Object> e = (Map.Entry) it.next();
            try {
                result.put(e.getKey(), e.getValue());
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
//        LogUtils.d("json=" + result.toString());
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), result.toString());
    }

    public static HashMap<String, String> addUdid(HashMap<String, String> params) {
        params.put("udid", Device.getDeviceId(App.getContext()));
        return params;
    }

}
