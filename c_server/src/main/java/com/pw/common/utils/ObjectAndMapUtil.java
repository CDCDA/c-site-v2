package com.pw.common.utils;

import lombok.SneakyThrows;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Object和Map互转
 */
public class ObjectAndMapUtil {
    // map 转 java 对象
    @SneakyThrows
    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz){
        if (map == null) return null;
        T t = clazz.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(t.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) setter.invoke(t, map.get(property.getName()));
        }
        return t;
    }

    // java 对象转 map
    @SneakyThrows
    public static Map<String, Object> objectToMap(Object obj){
        if (obj == null) return null;
        Map<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo
                .getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName();
            if (key.compareToIgnoreCase("class") == 0) continue;
            Method getter = property.getReadMethod();
            Object value = getter != null ? getter.invoke(obj) : null;
            map.put(key, value);
        }
        return map;
    }
}
