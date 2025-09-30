package com.pw.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pw.common.entity.BasePageDto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <博客>
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description  = "博客查询类")
public class BlogPageDTO extends BasePageDto {
    @TableField
    @Schema(description  = "博客id")
    private Long blogId;

    @Schema(description  = "博客标题")
    private String blogTitle;

    @Schema(description  = "用户id")
    private Long userId;

    @Schema(description  = "分类id")
    private Long typeId;

    @Schema(description  = "标签id")
    private Long tagId;

    @Schema(description  = "封面路径")
    private String coverUrl;

    @Schema(description  = "博客摘要")
    private String blogAbstract;

    //0：转载，1：原创
    @Schema(description  = "是否原创")
    private String isOriginal;

    //0：否，1：是
    @Schema(description  = "是否推荐")
    private String isRecommend;

    @Schema(description  = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date startTime;

    @TableField(exist = false)
    @Schema(description  = "结尾时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}
