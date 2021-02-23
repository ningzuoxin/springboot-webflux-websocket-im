package com.ning.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static long nowSecond() {
        return System.currentTimeMillis() / 1000;
    }

    public static String URLDecoderParam(String paramValue) {
        try {
            paramValue = URLDecoder.decode(paramValue, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return paramValue;

    }

    public static Map<String, String> getParamsFromQueryUrl(String url) {
        Map<String, String> result = new HashMap<>();
        String[] arrs = url.split("&");
        for (String s : arrs) {
            String[] params = s.split("=");
            result.put(params[0], params[1]);
        }
        return result;
    }

}
