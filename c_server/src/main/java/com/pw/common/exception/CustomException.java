package com.pw.common.exception;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 * @author cyd
 * @date 2023/6/20 17:51
 * @description <自定义全局异常类>
 **/
@Data
@Schema(description  = "自定义全局异常类")//Swagger注解
public class CustomException extends RuntimeException {

    @Schema(description  = "异常状态码")
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     *
     * @param message
     * @param code
     */
    public CustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    @Override
    public String toString() {
        return "CustomException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}

