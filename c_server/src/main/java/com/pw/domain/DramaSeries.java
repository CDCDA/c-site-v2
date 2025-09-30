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
 * @description <影视>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("drama_series")
@Schema(name = "影视")
public class DramaSeries extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "影视id")
    private Long id;

    @Schema(description = "影视分类(0:电影;1:电视剧;2:动漫)")
    private String type;

    @Schema(description = "封面")
    private String coverUrl;

    @Schema(description = "影视名称")
    private String name;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "影视播放地址")
    private String url;

    @Schema(description = "评分")
    private Float rate;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户id")
    private Long userId;
}
