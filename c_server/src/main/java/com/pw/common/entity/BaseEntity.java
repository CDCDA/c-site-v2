package com.pw.common.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 基类
 */
@Data
public class BaseEntity implements Serializable {
    private static final Long serialVersionUID = 1L;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description  = "创建者")
    private Long createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description  = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description  = "更新者")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(description  = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime ;

    @TableField(exist = false)
    @Schema(description  = "备注")
    private String remark;

    @TableField(exist = false)
    @Schema(description  = "起始页码")
    private Integer pageNum;

    @TableField(exist = false)
    @Schema(description  = "最大记录数")
    private Integer pageSize;

    @TableField(exist = false)
    @Schema(description  = "排序字段")
    private String orderBy;

    @TableField(exist = false)
    @Schema(description  = "排序方式")
    private String asc;


    /**
     * 初始化实体
     *
     * @param createdUserId
     */
    public void initialize(final Long createdUserId) {
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setCreateBy(createdUserId);
        this.setUpdateBy(createdUserId);
    }

    /**
     * 变更实体
     *
     * @param updateUserId
     */
    public void modify(final Long updateUserId) {
        this.setUpdateTime(new Date());
        this.setUpdateBy(updateUserId);
    }

}
