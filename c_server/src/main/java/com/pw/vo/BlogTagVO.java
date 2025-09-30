package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


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
@Schema(description  = "博客标签")
public class BlogTagVO {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "标签id")
    private Long tagId;

    @Schema(description  = "标签名")
    private String tagName;

    @Schema(description  = "标签封面")
    private String coverUrl;

    @Schema(description  = "标签文章数")
    private Integer total;

    @Schema(description  = "标签类型")
    private String tagType;

    @Schema(description  = "主题")
    private String effect;

    @Schema(description  = "颜色")
    private String color;




}
