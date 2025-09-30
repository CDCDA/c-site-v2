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
 * @description <壁纸>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("wallpaper")
@Schema(name = "壁纸")
public class Wallpaper extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "壁纸id")
    private Long id;

    @Schema(description = "壁纸类型(0:静态;1:动态)")
    private String type;

    @Schema(description = "壁纸路径")
    private String url;

    @Schema(description = "动态壁纸封面")
    private String coverUrl;

    @Schema(description = "壁纸名称")
    private String name;
}
