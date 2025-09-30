package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <用户>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("\"user\"")
@Schema(name = "用户")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class User extends BaseEntity implements Serializable {
    @TableId
    @Schema(description = "用户主键")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickName;

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String userName;

    @NotBlank(message = "用户密码不能为空")
    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户电话")
    private String phone;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "个人介绍")
    private String intro;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "博客网站")
    private String webUrl;

    @Schema(description = "ip属地")
    private String ipRegion;

    @Schema(description = "系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @TableField(exist = false)
    @Schema(description = "验证码")
    private String code;
}
