package com.pw.vo;

import com.fasterxml.jackson.annotation.JsonFormat;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/7/27 16:01
 * @description <>
 **/
@Data
@Schema(description  = "随笔统计")
public class EssayCountVO {
    @Schema(description  = "创建时间")
    private String createTime;

    @Schema(description  = "随笔数量")
    private String total;
}
