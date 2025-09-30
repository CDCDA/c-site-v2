package com.pw.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询基类
 */
@Data
public class BasePageDto extends BaseEntity implements Serializable {

    @TableField(exist = false)
    @Schema(description  = "起始页码")
    private Integer pageNum = 1;

    @TableField(exist = false)
    @Schema(description  = "最大记录数")
    private Integer pageSize = 10;

}
