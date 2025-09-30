package com.pw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.User;
import com.pw.mapper.UserMapper;
import com.pw.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    /* 检查 */
    @Override
    public Boolean checkPassword(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, user.getUserName())
                .eq(User::getPassword, user.getPassword());
        User loginUser = userMapper.selectList(wrapper).stream().findFirst().orElse(null);
        return ObjectUtils.isEmpty(loginUser) ? false : true;
    }

    @Override
    public User getUserByUserId(Long userId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserId, userId);
        return userMapper.selectList(wrapper).stream().findFirst().orElse(null);
    }
}
