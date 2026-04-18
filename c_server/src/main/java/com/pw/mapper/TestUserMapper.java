package com.pw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pw.domain.TestUser;
import org.springframework.stereotype.Repository;

/**
 * 测试用户Mapper接口
 *
 * @author cyd
 * @date 2026/04/07
 * @description 测试用户数据访问层
 */
@Repository
public interface TestUserMapper extends BaseMapper<TestUser> {
}
