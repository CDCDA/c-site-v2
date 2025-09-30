package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


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
@Schema(description  = "相册")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AlbumVO implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "主键")
    private Long id;

    @Schema(description  = "相册名")
    private String name;

    @Schema(description  = "简介")
    private String intro;

    @Schema(description  = "封面")
    private String coverUrl;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "创建时间")
    private String createTime;

    @Schema(description  = "图片路径集合")
    @TableField(exist = false)
    private List<ImageVO> images;
}
