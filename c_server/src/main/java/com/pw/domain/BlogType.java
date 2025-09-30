package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pw.common.entity.BaseEntity;
import com.pw.vo.BlogVO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <博客分类>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("blog_type")
@Schema(name = "博客分类")
public class BlogType extends BaseEntity implements Serializable {

    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "分类id")
    private Long typeId;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "父级id")
    private String parentId;

    @Schema(description = "分类名")
    private String typeName;

    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "分类封面")
    private String coverUrl;

    @Schema(description = "简介")
    private String intro;

    @TableField(exist = false)
    private List<BlogType> children;

    @TableField(exist = false)
    private List<BlogVO> blogList;
}
