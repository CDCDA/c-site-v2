package com.pw.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 * @author cyd
 * @date 2023/7/27 16:01
 * @description <>
 **/
@Data
@Schema(description  = "博客统计")
public class BlogTypeCountVO {
    @Schema(description  = "分类名")
    private String typeName;

    @Schema(description  = "博客数量")
    private String total;
}
