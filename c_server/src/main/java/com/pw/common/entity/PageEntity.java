package com.pw.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 基类
 */
@Data
public class PageEntity implements Serializable
{
    private static final Long serialVersionUID = 1L;

    @TableField(exist = false)
    @Schema(description  = "起始页码")
    private Integer pageNum;

    @TableField(exist = false)
    @Schema(description  = "最大记录数")
    private Integer pageSize;

}
