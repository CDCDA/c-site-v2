package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pw.common.entity.BasePageDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <用户>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
@Schema(description  = "用户")
public class UserDTO extends BasePageDto {
    @TableField
    @Schema(description  = "用户主键")
    private Long userId;

    @Schema(description  = "用户昵称")
    private String nickName;

    @Schema(description  = "用户账号")
    private String userName;

    @Schema(description  = "用户密码")
    private String password;

    @Schema(description  = "用户邮箱")
    private String email;

    @Schema(description  = "用户电话")
    private String phone;

    @Schema(description  = "用户头像")
    private String avatar;
}
