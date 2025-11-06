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
 * @description <消息>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message")
@Schema(name = "消息")
public class Message extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "消息id")
    private Long id;

    @Schema(description = "0:未读,1:已读")
    private String isRead;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "发送人id")
    private Long senderId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "接收人id")
    private Long receiverId;
}
