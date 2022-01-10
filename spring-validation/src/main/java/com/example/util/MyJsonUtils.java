package com.example.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public final class MyJsonUtils {
    public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJSONStringWithDateFormat(Object object, String dateFormat) {
        return JSON.toJSONStringWithDateFormat(object, dateFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String toJSONStringWithDateFormat(Object object) {
        return JSON.toJSONStringWithDateFormat(object, DEFAULT_DATE_FORMAT, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
    }
}
