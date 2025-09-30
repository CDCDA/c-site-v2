package com.pw.vo;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/***
 * @author cyd
 * @date 2023/7/27 16:01
 * @description <>
 **/
@Data
@Schema(description  = "游戏分类统计")
public class GameTypeCountVO {
    @Schema(description  = "分类")
    private String type;

    @Schema(description  = "游戏数量")
    private String total;
}
