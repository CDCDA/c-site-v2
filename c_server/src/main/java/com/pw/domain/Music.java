package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
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
@Schema(name = "歌曲")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Music extends BaseEntity implements Serializable {
    @TableId
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "歌曲名称")
    private String name;

    @Schema(description = "封面路径")
    private String coverUrl;

    @Schema(description = "歌曲路径")
    private String musicUrl;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户id")
    private String wyyId;

    @Schema(description = "发行时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date releaseTime;
}
