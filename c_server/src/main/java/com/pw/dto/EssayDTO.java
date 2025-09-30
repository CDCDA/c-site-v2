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
 * @description <随笔>
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("essay")
@Schema(description  = "随笔查询类")
public class EssayDTO extends BasePageDto {
    @TableId
    @Schema(description  = "主键")
    private Long id;

    @Schema(description  = "内容")
    private String content;

    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date startTime;


    @Schema(description  = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date endTime;

}
