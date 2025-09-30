package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author cyd
 * @date 2023/5/17 14:05
 * @description <标签博客关联>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_type")
@Schema(description  = "博客标签关联")
public class BlogTagRealationDTO {

    @TableField

    private Long blogId;

    @Schema(description  = "标签id")
    private Long tagId;

    @Schema(description  = "关联id")
    private Long relationId;

}
