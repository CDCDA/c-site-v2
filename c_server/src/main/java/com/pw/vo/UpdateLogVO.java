package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/5/24 17:59
 * @description <更新日志>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("update_log")
@Schema(description  = "更新日志")
public class UpdateLogVO {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "主键id")
    private Long id;

    @Schema(description  = "更新操作")
    private String operation;

    @Schema(description  = "创建时间")
    private Date createTime;
}
