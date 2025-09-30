package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <设备>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("equipment")
@Schema(name = "设备")
public class Equipment extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "设备id")
    private Long id;

    @Schema(description = "设备型号")
    private String model;

    @Schema(description = "封面")
    private String coverUrl;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "设备官网")
    private String url;

//    @Schema(description  = "评分")
//    private Float rate;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户id")
    private Long user_id;
}
