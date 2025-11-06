package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @description <消息>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message")
@Schema(name = "消息")
public class Todo extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "待办id")
    private Long id;

    @Schema(description = "待办标题")
    private String title;

    @Schema(description = "待办内容")
    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    private Date startTime;

    @Schema(description = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    private Date endTime;

    @Schema(description = "预计时间")
    private String estimatedTime;

    @Schema(description = "预计时间单位")
    private String timeUnit;

    @Schema(description = "优先级")
    private String priority;

    @Schema(description = "状态(0:未开始,1:进行中,2:已完成)")
    private String status;


}
