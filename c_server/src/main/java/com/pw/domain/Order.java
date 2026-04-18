package com.pw.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单实体类
 *
 * @author cyd
 * @date 2026/04/07
 * @description 订单表（分库分表测试）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_order")
@Schema(name = "订单")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class Order implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "订单ID，分布式ID（分片键）")
    private Long id;

    @Schema(description = "订单号（业务唯一键）")
    private String orderNo;

    @Schema(description = "用户ID（分片键候选）")
    private Long userId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "订单金额")
    private BigDecimal amount;

    @Schema(description = "订单状态：0待支付，1已支付，2已发货，3已完成，4已取消")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "支付时间")
    private Date payTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;
}
