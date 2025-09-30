package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableName;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class BlogTypeDataVO {

    @Schema(description  = "博客分类")
    private String typeName;

    @Schema(description  = "博客分类id")
    private String typeId;

    @Schema(description  = "分类下博客数量")
    private String count;

    @Schema(description  = "博客列表")
    private List<BlogVO> blogList;
}
