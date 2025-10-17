package com.pw.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Map;

import static com.pw.common.utils.CamelAndUnderlineUntil.camelUnder;

public class ConvertWrapper {
    public static <T> QueryWrapper<T> convertWrap(T entity) {
        Map<String, Object> params = ObjectAndMapUtil.objectToMap(entity);
        return mapToWrap(params);
    }

    public static <T> QueryWrapper<T> convertEqWrap(T entity) {
        Map<String, Object> params = ObjectAndMapUtil.objectToMap(entity);
        return mapToEqWrap(params);
    }

    public static <T> QueryWrapper<T> mapToWrap(Map<String, Object> map) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        map.forEach((key, value) -> {
            //包含id使用全等
            if (value != null && (key.contains("id") || key.contains("Id"))) {
                wrapper.eq(camelUnder(key), value);
            }
            //其他除特殊固定key外,使用包含
            else if (value != null && !key.equals("pageNum") && !key.equals("pageSize")
                    && !key.equals("startTime") && !key.equals("endTime")
                    && !key.equals("orderBy") && !key.equals("asc") && !value.equals("")) {
                wrapper.like(camelUnder(key), value);
            }
        });
        //时间范围
        if (ObjectUtils.isNotEmpty(map.get("startTime")) && ObjectUtils.isNotEmpty(map.get("endTime"))) {
            wrapper.between("create_time", map.get("startTime"), map.get("endTime"));
        }
        if (ObjectUtils.isEmpty(map.get("asc"))) {
            if (ObjectUtils.isEmpty(map.get("orderBy"))) {
                wrapper.orderByDesc("create_time");
            } else {
                wrapper.orderByDesc((String) map.get("orderBy"));
            }
        } else {
            if (ObjectUtils.isEmpty(map.get("orderBy"))) {
                wrapper.orderByAsc("create_time");
            } else {
                wrapper.orderByAsc((String) map.get("orderBy"));
            }
        }


        return wrapper;
    }

    public static <T> QueryWrapper<T> mapToEqWrap(Map<String, Object> map) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        map.forEach((key, value) -> {
            if (value != null && !key.equals("pageNum") && !key.equals("pageSize") && !key.equals("startTime") && !key.equals("endTime") && !key.equals("orderBy") && !key.equals("asc") && !value.equals("")) {
                wrapper.eq(camelUnder(key), value);
            }
        });
        if (ObjectUtils.isNotEmpty(map.get("startTime")) && ObjectUtils.isNotEmpty(map.get("endTime"))) {
            wrapper.between("create_time", map.get("startTime"), map.get("endTime"));
        }
        if (ObjectUtils.isEmpty(map.get("asc"))) {
            if (ObjectUtils.isEmpty(map.get("orderBy"))) {
                wrapper.orderByDesc("create_time");
            } else {
                wrapper.orderByDesc((String) map.get("orderBy"));
            }
        } else {
            if (ObjectUtils.isEmpty(map.get("orderBy"))) {
                wrapper.orderByAsc("create_time");
            } else {
                wrapper.orderByAsc((String) map.get("orderBy"));
            }
        }
        return wrapper;
    }

}
