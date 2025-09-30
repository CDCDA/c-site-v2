package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pw.common.entity.BasePageDto;


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
@Schema(description  = "博客标签查询类")
public class BlogTagDTO extends BasePageDto {

    @Schema(description  = "标签名")
    private String tagName;

    @Schema(description  = "用户id")
    private Long userId;

}
