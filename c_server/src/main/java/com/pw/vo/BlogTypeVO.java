package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <博客分类>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_type")
@Schema(description  = "博客分类")
public class BlogTypeVO {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "分类id")
    private Long typeId;

    @Schema(description  = "父级id")
    private Long parentId;

    @Schema(description  = "分类名")
    private String typeName;

    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "分类封面")
    private String coverUrl;

    @Schema(description  = "简介")
    private String intro;

    @Schema(description  = "分类博客数")
    private Integer total;

    @Schema(description  = "博客列表")
    private List<BlogVO> blogList;

}
