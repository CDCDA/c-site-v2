package com.pw.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.TestUser;
import com.pw.mapper.TestUserMapper;
import org.springframework.stereotype.Service;

/**
 * 测试用户服务实现类
 *
 * @author cyd
 * @date 2026/04/07
 * @description 测试用户服务实现，提供批量操作支持
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> {
}
