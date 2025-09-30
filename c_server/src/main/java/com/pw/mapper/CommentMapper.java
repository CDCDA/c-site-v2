package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.Comment;
import org.springframework.stereotype.Repository;

@Repository//代表持久层
public interface CommentMapper extends BaseMapper<Comment> {

}
