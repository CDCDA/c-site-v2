package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/***
 * @author cyd
 * @date 2024/10/11 13:32
 * @description <>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
@Schema(name = "微信公众号模板消息")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TemplateMessage {

    @Schema(description = "发送对象")
    private String touser;

    @Schema(description = "模板id")
    private String template_id;

    @Schema(description = "小程序跳转")
    private Map<String, String> miniprogram;

    @Schema(description = "参数")
    private Map<String, String> data;


}
