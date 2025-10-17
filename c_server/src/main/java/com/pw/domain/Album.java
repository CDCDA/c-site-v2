package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <相册>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("album")
@Schema(name = "相册")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Album extends BaseEntity implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "相册名")
    private String name;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "封面")
    private String coverUrl;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "图片关联")
    @TableField(exist = false)
    private List<ImageRelation> images;
}
