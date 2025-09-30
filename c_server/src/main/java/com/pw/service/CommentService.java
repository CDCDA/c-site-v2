package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.Comment;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <评论接口>
 **/

public interface CommentService extends IService<Comment> {
    List<Comment> listTree(Comment comment);

}
