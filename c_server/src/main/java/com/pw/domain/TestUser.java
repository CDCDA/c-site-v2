package com.pw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 测试用户实体类
 *
 * @author cyd
 * @date 2026/04/07
 * @description 用户表（大批量数据测试）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
@Schema(name = "测试用户")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class TestUser implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "手机号")
    private String phone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "注册时间")
    private Date registerTime;
}
