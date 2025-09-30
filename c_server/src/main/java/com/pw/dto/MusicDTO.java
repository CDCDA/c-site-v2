package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.pw.common.entity.BasePageDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <歌曲>
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("music")
@Schema(description  = "歌曲查询类")
public class MusicDTO extends BasePageDto {
    @TableId
    @Schema(description  = "主键")
    private Long id;

    @Schema(description  = "相册名")
    private String name;

    @Schema(description  = "简介")
    private String intro;

    @Schema(description  = "封面")
    private String coverUrl;

    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    @Schema(description  = "结尾时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
