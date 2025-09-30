package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <图片>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description  = "图片")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ImageVO implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "主键")
    private Long id;

    @Schema(description  = "图片路径")
    private String url;

    @Schema(description  = "创建时间")
    private String createTime;
}
