package com.pw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.User;
import com.pw.mapper.UserMapper;
import com.pw.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    private static final String USER_CACHE_PREFIX = "jwt:user:";

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

    @Override
    public boolean updateById(User entity) {
        // 清除用户缓存
        String cacheKey = USER_CACHE_PREFIX + entity.getUserId();
        redisTemplate.delete(cacheKey);
        log.info("[JWT优化] 用户 {} 信息更新，清除缓存", entity.getUserId());
        return super.updateById(entity);
    }
}
