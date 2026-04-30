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
 * 读写分离测试实体类
 *
 * @author cyd
 * @date 2026/04/24
 * @description 读写分离测试表（主库C_PW写入，从库C_PW2读取）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_read_write_test")
@Schema(name = "读写分离测试")
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ReadWriteTest implements Serializable {

    @TableId(type = IdType.AUTO)
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "状态")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private Date updateTime;
}
