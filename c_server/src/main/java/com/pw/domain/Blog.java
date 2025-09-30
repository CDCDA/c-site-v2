package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <博客>
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
@Schema(name = "博客")
public class Blog extends BaseEntity {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(name = "博客id")
    private Long blogId;

    @Schema(description = "博客标题")
    private String blogTitle;

    @Schema(description = "博客内容")
    private String content;

    @Schema(description = "博客markdown内容")
    private String mkContent;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "分类id")
    private Long typeId;

    @Schema(description = "封面路径")
    private String coverUrl;

    @Schema(description = "博客摘要")
    private String blogAbstract;

    //0：原创，1：转载
    @Schema(description = "是否原创")
    private String isOriginal;

    //0：否，1：是
    @Schema(description = "是否推荐")
    private String isRecommend;

    @Schema(description = "标签列表")
    @TableField(exist = false)
    private List<BlogTag> tags;
}
