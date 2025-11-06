package com.pw.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;
import com.pw.common.handler.CustomWebSocketHandler;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "消息通知")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Notification extends BaseEntity implements Serializable {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态 primary:普通;warning:警告;error:危险")
    private String status;

    @Schema(description = "频道 disk_info:磁盘信息频道;todo_list:待办事项频道;system_notice:系统通知频道")
    private String channels;
}
