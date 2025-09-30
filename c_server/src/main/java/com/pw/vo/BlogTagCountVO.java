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
public class BlogTagCountVO {
    @Schema(description  = "标签名")
    private String tagName;

    @Schema(description  = "博客数量")
    private String total;
}
