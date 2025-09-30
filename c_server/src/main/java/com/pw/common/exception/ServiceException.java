package com.pw.common.exception;

/***
 * @author cyd
 * @date 2023/6/20 18:03
 * @description <全局异常处理>
 **/
public final class ServiceException extends RuntimeException {
    private static final Long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误提示
     */
    private String message;


    /**
     * 空构造方法，避免反序列化问题
     */
    public ServiceException() {
    }

    public ServiceException(Integer code, String message, Throwable e) {
        super(message, e);
        this.message = message;
        this.code = code;
    }

    public ServiceException(Integer code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public ServiceException(String message) {
        this(null, message);
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

}
