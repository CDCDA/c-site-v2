package com.pw.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pw.dto.BlogPageDTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一结果返回类。方法采用链式调用的写法（即返回类本身 return this）。
 * 构造器私有，不允许进行实例化，但提供静态方法 ok、error 返回一个实例。
 * 静态方法说明：
 * ok     返回一个 成功操作 的结果（实例对象）。
 * error  返回一个 失败操作 的结果（实例对象）。
 * <p>
 * 普通方法说明：
 * success      用于自定义响应是否成功
 * code         用于自定义响应状态码
 * message      用于自定义响应消息
 * data         用于自定义响应数据
 */
@Data
public class Result<T> extends HashMap<String, Object> {
    /**
     * 响应状态码， 200 成功，500 系统异常
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 私有自定义构造器
     *
     * @param code    响应状态码
     * @param message 响应消息
     */
    private Result(Integer code, String message) {
        super.put("code", code);
        super.put("msg", message);
    }

    private Result(Integer code, String message, T data) {
        super.put("code", code);
        super.put("msg", message);
        super.put("data", data);
    }

    private Result(Integer code, String message, IPage<T> iPage) {
        super.put("code", code);
        super.put("msg", message);
        super.put("rows", iPage.getRecords());
        super.put("total", iPage.getTotal());
    }

    private Result() {
        super.put("code", 200);
        super.put("msg", "成功");
    }

    /**
     * 返回一个默认的 成功操作 的结果，默认响应状态码 200
     *
     * @return 成功操作的实例对象
     */
    public static Result ok() {
        return new Result(HttpStatus.OK.value(), "success");
    }

    public static Result ok(String message) {
        return new Result(HttpStatus.OK.value(), message);
    }

    /**
     * 返回一个自定义 成功操作 的结果
     *
     * @param code    响应状态码
     * @param message 响应消息
     * @return 成功操作的实例对象
     */
    public static Result ok(Integer code, String message) {
        return new Result(code, message);
    }

    /**
     * 返回一个默认的 失败操作 的结果，默认响应状态码为 500
     *
     * @return 失败操作的实例对象
     */
    public static Result error() {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), "error");
    }

    public static Result error(String message) {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }

    /**
     * 返回一个自定义 失败操作 的结果
     *
     * @param code    响应状态码
     * @param message 相应消息
     * @return 失败操作的实例对象
     */
    public static Result error(Integer code, String message) {
        return new Result(code, message);
    }

    /**
     * 自定义响应状态码
     *
     * @param code 响应状态码
     * @return 当前实例对象
     */
    public Result code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 自定义响应消息
     *
     * @param message 响应消息
     * @return 当前实例对象
     */
    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * 自定义响应数据，一次设置一个 map 集合
     *
     * @param map 响应数据
     * @return 当前实例对象
     */
    public Result data(Map<String, Object> map) {
        super.put("data", map);
        return this;
    }

    /**
     * 自定义响应数据，一次设置一个 list 集合
     *
     * @param list 响应数据
     * @return 当前实例对象
     */
    public Result data(List<T> list) {
        super.put("data", list);
        return this;
    }

    /**
     * 自定义响应数据，一次设置一个 list 集合
     *
     * @param result 响应数据
     * @return 当前实例对象
     */
    public Result data(T result) {
        super.put("data", result);
        return this;
    }

    /**
     * 通用设置响应数据，一次设置一个 key - value 键值对
     *
     * @param key   键
     * @param value 数据
     * @return 当前实例对象
     */
    public Result data(String key, Object value) {
        HashMap map = new HashMap<>();
        map.put(key, value);
        super.put("data", map);
        return this;
    }


    public Result page(IPage<T> iPage) {
        super.put("rows", iPage.getRecords());
        super.put("total", iPage.getTotal());
        return this;
    }

    public Result page(T rows, Integer total) {
        super.put("rows", rows);
        super.put("total", total);
        return this;
    }
}
