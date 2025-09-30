package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author cyd
 * @date 2024/10/11 13:32
 * @description <>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
@Schema(name = "微信公众号消息模板")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeixinTemplate {

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "模板id")
    private String template_id;

    @Schema(description = "模板标题")
    private String title;

    @Schema(description = "模板内容")

    private String content;

    @Schema(description = "模板样例")
    private String example;

    @Schema(description = "")
    private String primary_industry;

    @Schema(description = "")
    private String deputy_industry;


}
