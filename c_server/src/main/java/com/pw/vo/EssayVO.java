package com.pw.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <随笔>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("essay")
@Schema(description  = "随笔")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EssayVO implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "主键")
    private Long id;

    @Schema(description  = "内容")
    private String content;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "标签")
    private String tags;

    @Schema(description  = "图片路径集合")
    @TableField(exist = false)
    private List<String> images;

    @Schema(description  = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
