package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * @author cyd
 * @date 2025/10/24 10:39
 * @description <>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "修改密码")
public class UserPasswordDTO {

//    @NotBlank(message = "用户id不能为空")
//    @Schema(description = "用户id")
//    private Long userId;

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码")
    private String newPassword;
}
