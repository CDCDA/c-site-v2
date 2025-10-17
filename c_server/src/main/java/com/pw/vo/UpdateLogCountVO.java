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
@Schema(description  = "更新统计")
public class UpdateLogCountVO {

    @Schema(description  = "操作更新间")
    private String operateTime;

    @Schema(description  = "博客数量")
    private String total;
}
