package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <图片关联>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("image_relation")
@Schema(name = "图片关联")
public class ImageRelation {
    @TableId
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "随笔id")
    private Long essayId;

    @Schema(description = "相册id")
    private Long albumId;

    @Schema(description = "图片路径")
    private String url;
}
