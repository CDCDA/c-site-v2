package com.pw.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 * @author cyd
 * @date 2023/7/27 16:01
 * @description <>
 **/
@Data
@Schema(description  = "影视分类统计")
public class DramaTypeCountVO {
    @Schema(description  = "分类")
    private String type;

    @Schema(description  = "影视数量")
    private String total;
}
