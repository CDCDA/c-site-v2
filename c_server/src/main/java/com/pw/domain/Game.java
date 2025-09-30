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
 * @description <游戏>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("game")
@Schema(name = "游戏")
public class Game extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "游戏id")
    private Long id;

    @Schema(description = "游戏类型(0:单机;1:手游)")
    private String type;

    @Schema(description = "封面")
    private String coverUrl;

    @Schema(description = "游戏名称")
    private String name;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "游戏官网")
    private String url;

    @Schema(description = "评分")
    private Float rate;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户id")
    private Long userId;
}
