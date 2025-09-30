package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BaseEntity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <微信校验类>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "微信校验类")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WeixinVerify extends BaseEntity implements Serializable {
    @TableId
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "相册名")
    private String name;

    @Schema(description = "简介")
    private String intro;

    @Schema(description = "封面")
    private String coverUrl;
}
