package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/24 17:59
 * @description <>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
@Schema(description  = "博客")
public class BlogVO {
    @TableField
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "博客id")
    private Long blogId;

    @Schema(description  = "博客标题")
    private String blogTitle;

    @Schema(description  = "博客内容")
    private String content;

    @Schema(description  = "博客markdown内容")
    private String mkContent;

    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "分类id")
    private String typeId;

    @Schema(description  = "分类")
    private String typeName;

    @Schema(description  = "封面路径")
    private String coverUrl;

    @Schema(description  = "博客摘要")
    private String blogAbstract;

    //0：转载，1：原创
    @Schema(description  = "是否原创")
    private String isOriginal;

    @Schema(description  = "作者")
    private String author;

    //0：否，1：是
    @Schema(description  = "是否推荐")
    private String isRecommend;

    @Schema(description  = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Schema(description  = "标签列表")
    @TableField(exist = false)
    private List<BlogTagVO> tags;

}
