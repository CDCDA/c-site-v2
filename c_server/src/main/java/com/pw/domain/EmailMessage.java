package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <邮件>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("email")
@Schema(name = "邮件")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailMessage extends BaseEntity implements Serializable {

    @Schema(description = "邮箱")
    private String to;

    @Schema(description = "内容")
    private String text;

    @Schema(description = "主题")
    private String subject;
}
