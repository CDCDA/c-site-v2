package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Todo;
import com.pw.mapper.TodoMapper;
import com.pw.service.TodoService;
import org.springframework.stereotype.Service;

/***
 * @author cyd
 * @date 2023/5/18 17:52
 * @description <>
 **/
@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, Todo> implements TodoService {

}
