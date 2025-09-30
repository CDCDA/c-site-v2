package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <博客标签>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_tag")
@Schema(name = "博客标签")
public class BlogTag extends BaseEntity {

    @TableId
    @Schema(description = "标签id")
    private Long tagId;

    @Schema(description = "标签名")
    private String tagName;

    @TableField(exist = false)
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标签类型")
    private String tagType;

    @Schema(description = "主题")
    private String effect;

    @Schema(description = "颜色")
    private String color;


}
