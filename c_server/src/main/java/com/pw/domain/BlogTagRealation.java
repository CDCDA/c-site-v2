package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("blog_tag_relation")
@Schema(name = "博客标签关联")
public class BlogTagRealation {

    @Schema(description = "博客id")
    private Long blogId;

    @Schema(description = "标签名")
    private Long tagName;

    @TableId
    @Schema(description = "关联id")
    private Long relationId;

}
