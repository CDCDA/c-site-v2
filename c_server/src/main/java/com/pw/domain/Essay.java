package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <随笔>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("essay")
@Schema(name = "随笔")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Essay extends BaseEntity implements Serializable {
    @TableId
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标签")
    private String tags;

    @Schema(description = "图片关联")
    @TableField(exist = false)
    private List<ImageRelation> imageRelations;
}
