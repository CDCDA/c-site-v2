package com.pw.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtil {
    /**
     * 验证结果是否存在--T
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result resultExit(T result) {
        if (result == null || result.equals(false)) {
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 验证结果是否存在--boolean
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result resultExit(Boolean result) {
        if (result == false) {
            return Result.error();
        }
        return Result.ok();
    }

    /**
     * 验证结果是否存在--Integer
     *
     * @param result
     * @param
     * @return
     */
    public static Result resultExit(Integer result) {
        return Result.ok();
    }

    /**
     * Ipage<T>转为Result类型
     *
     * @param result
     * @param <T>
     * @return
     */
    public static <T> Result resultIPage(IPage<T> result) {
        if (result == null) {
            return Result.error();
        }
        return Result.ok().page(result);
    }

    public static <T> Result resultPage(T rows, Integer total) {
        return Result.ok().page(rows, total);
    }

    /**
     * list转为Result类型
     *
     * @param list
     * @param total
     * @param <T>
     * @return
     */
    public static <T> Result resultList(List<T> list, Integer total) {
        if (list == null) {
            return Result.error();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("total", total);
        data.put("list", list);
        return Result.ok().data(data);
    }

    public static <T> Result resultList(List<T> list) {
        if (list == null || list.size() == 0) {
            return Result.error();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("list", list);
        return Result.ok().data(data);
    }

    public static <T> Result resultData(T result) {
        return Result.ok().data(result);
    }

    public static <T> Result resultData(List<T> list) {
        return Result.ok().data(list);
    }
}
