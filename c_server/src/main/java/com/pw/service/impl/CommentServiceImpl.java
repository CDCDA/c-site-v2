package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.common.utils.ArraysToTreeUtil;
import com.pw.domain.Comment;
import com.pw.mapper.CommentMapper;
import com.pw.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.pw.common.utils.ConvertWrapper.convertWrap;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List listTree(Comment comment) {
        List<Comment> commentList = this.list(convertWrap(comment));
        commentList.forEach(x->{
            x.setChildren(new ArrayList<>());
        });
        ArraysToTreeUtil<Comment> arraysToTreeUtil = new ArraysToTreeUtil<>();
        arraysToTreeUtil.parent(Comment::getParentId).children(Comment::getChildren).code(Comment::getId);
        return arraysToTreeUtil.tree(commentList);
    }
}
