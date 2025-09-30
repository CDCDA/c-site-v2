package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class BlogTypeDTO {

    @TableField
    @Schema(description  = "分类id")
    private Long typeId;

    @Schema(description  = "父级id")
    private Long parentId;

    @Schema(description  = "分类名")
    private String typeName;

    @Schema(description  = "用户id")
    private Long userId;

}
