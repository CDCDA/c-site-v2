package com.pw.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/6/5 17:19
 * @description <>
 **/
@Data
public class blogVo {
    @Schema(description  = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description  = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description  = "更新者")
    @TableField(exist = false)
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(description  = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
