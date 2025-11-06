package com.pw.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(name = "系统通知")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SystemNotification extends BaseEntity implements Serializable {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态 0:普通;1:警告;2:危险")
    private String status;
}
