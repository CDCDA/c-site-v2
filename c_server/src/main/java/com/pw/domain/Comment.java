package com.pw.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pw.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/***
 * @author cyd
 * @date 2023/5/17 11:35
 * @description <评论>
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
@Schema(name = "评论")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Comment extends BaseEntity implements Serializable {
    @TableId
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "用户")
    private Long userId;

    @Schema(description = "评论内容")
    private String comment;

    @Schema(description = "ip属地")
    private String ipRegion;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "系统")
    private String os;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "父级回复id")
    private Long parentId;

    @Schema(description = "关联id")
    private Long relevanceId;

    @Schema(description = "类型:0:博客；1:随笔；2:相册；3:游戏；4:影视；5:网站；")
    private String type;

    @Schema(description = "博客网站地址")
    private String webUrl;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "回复评论")
    @TableField(exist = false)
    private List<Comment> children;

}
